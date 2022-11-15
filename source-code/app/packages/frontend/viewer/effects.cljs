
(ns app.packages.frontend.viewer.effects
    (:require [app.packages.frontend.viewer.queries    :as viewer.queries]
              [app.packages.frontend.viewer.validators :as viewer.validators]
              [app.packages.frontend.viewer.views      :as viewer.views]
              [re-frame.api                            :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :packages.viewer/load-viewer!
  ; @param (keyword) view-id
  (fn [_ [_ view-id]]
      {:dispatch-n [[:x.gestures/init-view-handler! :packages.viewer
                                                    {:default-view-id (or view-id :overview)}]
                    [:x.ui/render-surface! :packages.viewer/view
                                           {:content #'viewer.views/view}]]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :packages.viewer/save-package-products!
  (fn [{:keys [db]} [_ package-products]]
      (let [query        (r viewer.queries/get-save-package-products-query          db package-products)
            validator-f #(r viewer.validators/save-package-products-response-valid? db %)]
           [:pathom/send-query! :packages.viewer/save-package-products!
                                {:on-failure [:packages.viewer/save-package-products-failed]
                                 :query query :validator-f validator-f}])))

(r/reg-event-fx :packages.viewer/save-package-products-failed
  [:x.ui/render-bubble! ::notification
                        {:body :failed-to-save}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :packages.viewer/save-package-services!
  (fn [{:keys [db]} [_ package-services]]
      (let [query        (r viewer.queries/get-save-package-services-query          db package-services)
            validator-f #(r viewer.validators/save-package-services-response-valid? db %)]
           [:pathom/send-query! :packages.viewer/save-package-services!
                                {:on-failure [:packages.viewer/save-package-services-failed]
                                 :query query :validator-f validator-f}])))

(r/reg-event-fx :packages.viewer/save-package-services-failed
  [:x.ui/render-bubble! ::notification
                        {:body :failed-to-save}])
