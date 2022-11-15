
(ns app.views.frontend.no-internet-notification.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-browser-offline [:views.no-internet-notification/blow-no-internet-bubble?!]
   :on-app-launch      [:views.no-internet-notification/blow-no-internet-bubble?!]
   :on-browser-online  [:x.ui/remove-bubble! :views.no-internet-notification/notification]})
