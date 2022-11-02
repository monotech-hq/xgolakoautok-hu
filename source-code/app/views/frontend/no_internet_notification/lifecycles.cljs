
(ns app.views.frontend.no-internet-notification.lifecycles
    (:require [x.app-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-browser-offline [:views.no-internet-notification/blow-no-internet-bubble?!]
   :on-app-launch      [:views.no-internet-notification/blow-no-internet-bubble?!]
   :on-browser-online  [:ui/remove-bubble! :views.no-internet-notification/notification]})
