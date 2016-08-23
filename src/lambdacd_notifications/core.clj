(ns lambdacd-notifications.core
  (:require [clojure.core.async :as async]
            [lambdacd.event-bus :as event-bus]
            [lambdacd.internal.pipeline-state :as internal-pipeline-state]
            [lambdacd.presentation.pipeline-state :as presentation-pipeline-state]))



(defn- notify [notifier pipeline-state-component event ignored-statuses]
  (let [build-number                           (:build-number event)
        step-ids-and-results-for-all-builds    (internal-pipeline-state/get-all pipeline-state-component)
        step-ids-and-results-for-current-build (get step-ids-and-results-for-all-builds build-number)
        overall-build-status                   (presentation-pipeline-state/overall-build-status step-ids-and-results-for-current-build)
        build-duration                         (presentation-pipeline-state/build-duration step-ids-and-results-for-current-build)]
    (when (not-any? #(= overall-build-status %) ignored-statuses)
      (.notify notifier {:status       overall-build-status
                         :duration     build-duration
                         :build-number build-number
                         :event event}))))


(defn setup
  ([pipeline ignored-statuses notifier]
   (let [pipeline-context    (:context pipeline)
         subscription        (event-bus/subscribe pipeline-context :step-finished)
         steps-finished-chan (event-bus/only-payload subscription)]
     (async/go-loop []
       (when-let [event (async/<! steps-finished-chan)]
         (notify notifier (:pipeline-state-component pipeline-context) event ignored-statuses))
       (recur))))
  ([pipeline notifier]
   (setup pipeline [:waiting :running] notifier)))
