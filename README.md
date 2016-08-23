# lambdacd-notifications

This is still a WIP. Please do not start using this library until you see this warning disappear.

This library is intended for notifying final build status through channels like slack, email, hipchat, etc...

##Screenshot

![image](https://cloud.githubusercontent.com/assets/919715/17907660/899e0b54-699a-11e6-9768-56e64eabff1b.png)

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
