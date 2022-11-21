
(ns site.xgo.pages.main-page.frontend.effects
    (:require [re-frame.api                            :as r]
              [x.router.api                            :as router]
              [site.xgo.pages.main-page.frontend.views :as views]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(r/reg-event-fx :main-page/render-page!
  (fn [{:keys [db]} [_]]
    (let [path-params (router/get-current-route-path-params db)]
      {:dispatch-n     [[:x.db/set-item! [:filters] path-params]
                        [:x.ui/render-surface! :main-page/view {:content #'views/view}]]})))

(r/reg-event-fx :main-page/load-page!
  [:main-page/render-page!])
