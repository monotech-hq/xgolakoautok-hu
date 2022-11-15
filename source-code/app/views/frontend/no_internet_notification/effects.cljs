
(ns app.views.frontend.no-internet-notification.effects
    (:require [app.views.frontend.no-internet-notification.views :as no-internet-notification.views]
              [re-frame.api                                      :as r :refer [r]]
              [x.environment.api                                 :as x.environment]
              [x.ui.api                                          :as x.ui]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :views.no-internet-notification/blow-no-internet-bubble?!
  (fn [{:keys [db]} _]
      (if (and (r x.environment/browser-offline? db)
               (r x.ui/application-interface?    db))
          [:x.ui/render-bubble! :views.no-internet-notification/notification
                                {:body        #'no-internet-notification.views/body
                                 :autoclose?  false
                                 :user-close? false}])))
