
(ns site.xgo.pages.price-quote.frontend.effects
  (:require [re-frame.api :as r]
            [site.xgo.pages.price-quote.frontend.views :as views]
            [x.router.api :as router]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(r/reg-event-fx :price-quote/render-page!
  (fn [{:keys [db]} [_]]
    (let [path-params (router/get-current-route-path-params db)]
      {:dispatch-n [[:x.db/set-item! [:filters] path-params]
                    [:x.ui/render-surface! :price-quote/view {:content #'views/view}]]})))

(r/reg-event-fx :price-quote/load-page!
  [:price-quote/render-page!])
