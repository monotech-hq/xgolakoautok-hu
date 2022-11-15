
(ns app.views.frontend.menu-screen.effects
    (:require [app.views.frontend.menu-screen.views :as menu-screen.views]
              [re-frame.api                         :as r :refer [r]]
              [x.gestures.api                       :as x.gestures]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :views.menu-screen/render!
  (fn [{:keys [db]} _]
      {:db       (r x.gestures/init-view-handler! db :views.menu-screen/handler {:default-view-id :main})
       :dispatch [:x.ui/render-popup! :views.menu-screen/view
                                      {:content #'menu-screen.views/view}]}))
