(ns lambdacd-notifications.slack.core
  (:require [lambdacd.util :as util]
            [lambdacd-notifications.notifier :refer [Notifier]]
            [clojure.string :as string]
            [clj-http.client :as http-client]
            [clojure.data.json :as json])
  (:import  [java.net.URL]))

(defn ->color [status]
  (case status
    :success "good"
    :failure "danger"
    :waiting "warning"
    :running "good"
    :killed  "danger"
    :unknown "danger"))

(defn ->status-message [status]
  (case status
    :success "Success"
    :failure "Failed"
    :waiting "Waiting"
    :running "Running"
    :killed  "Killed"
    :unknown "Unknown"))

(defn ->build-url [ci-host build-number]
  (.toString (java.net.URL. (java.net.URL. ci-host) (str "/#/builds/" build-number))))

(defn build-payload [ci-host {:keys [build-number status duration event]}]
  (json/write-str {:attachments [{:title      (str "Build #" build-number ": " (->status-message status))
                                  :title_link (->build-url ci-host build-number)
                                  :text       (get-in event [:final-result :out])
                                  :footer     (str "Took " duration " seconds to run")
                                  :color      (->color status)}]}))

(defrecord SlackNotifier [webhook-url ci-host]
  Notifier
  (notify [this overall-build-info]
    (http-client/post webhook-url {:form-params {:payload (build-payload ci-host overall-build-info)}})))
