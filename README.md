# lambdacd-notifications

This is still a WIP. Please do not start using this library until you see this warning disappear.

This library is intended for notifying final build status through channels like slack, email, hipchat, etc...

##Screenshot

TBD

##Usage

```clojure

(require '[lambdacd-notifications.core :as notifications])
(require '[lambdacd-notifications.slack.core :as slack])

(def slack-notifier (slack/map->SlackNotifier {:webhook-url "<insert-your-slack-webhook-url>"
                                               :ci-host     "<ci-server-host-url>"}))

(notifications/setup pipeline slack-notifier)

```

Currently, only slack notification is supported in the library and I am planning to add more notifiers in the future.
But, you could create your own notifier by extending the `Notifier` protocol.
