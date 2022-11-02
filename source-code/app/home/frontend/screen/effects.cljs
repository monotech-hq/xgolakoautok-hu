
(ns app.home.frontend.screen.effects
    (:require [app.home.frontend.screen.events :as screen.events]
              [app.home.frontend.screen.views  :as screen.views]
              [re-frame.api                    :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :home.screen/render-screen!
  [:ui/render-surface! :home.screen/view
                       {:content #'screen.views/view}])

(r/reg-event-fx :home.screen/load-screen!
  (fn [{:keys [db]} _]
      {:db             (r screen.events/load-screen! db)
       :dispatch-n     [[:ui/simulate-process!]
                        [:home.screen/render-screen!]
                        [:ui/restore-default-window-title!]]
       :dispatch-later [{:ms 500 :dispatch [:home.screen/screen-loaded]}]}))
