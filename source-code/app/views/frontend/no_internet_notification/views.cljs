
(ns app.views.frontend.no-internet-notification.views
    (:require [elements.api :as elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- no-internet-connection-label
  []
  [elements/label ::no-internet-connection-label
                  {:content     :no-internet-connection
                   :indent      {:left :xs}
                   :line-height :block}])

(defn- refresh-app-button
  []
  [elements/button ::refresh-app-button
                   {:indent   {:right :xs}
                    :label    :refresh!
                    :on-click [:boot-loader/refresh-app!]
                    :preset   :primary}])

(defn body
  [bubble-id]
  [elements/horizontal-polarity ::body
                                {:end-content   [refresh-app-button]
                                 :start-content [no-internet-connection-label]}])
