(ns lambdacd-notifications.notifier)

(defprotocol Notifier
  (notify [this overall-build-info]))
