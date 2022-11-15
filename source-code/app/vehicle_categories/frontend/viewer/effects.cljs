
(ns app.vehicle-categories.frontend.viewer.effects
    (:require [app.vehicle-categories.frontend.viewer.queries    :as viewer.queries]
              [app.vehicle-categories.frontend.viewer.validators :as viewer.validators]
              [app.vehicle-categories.frontend.viewer.views      :as viewer.views]
              [re-frame.api                                      :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :vehicle-categories.viewer/load-viewer!
  ; @param (keyword) view-id
  (fn [_ [_ view-id]]
      {:dispatch-n [[:x.gestures/init-view-handler! :vehicle-categories.viewer
                                                    {:default-view-id (or view-id :overview)}]
                    [:x.ui/render-surface! :vehicle-categories.viewer/view
                                           {:content #'viewer.views/view}]]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :vehicle-categories.viewer/save-category-models!
  (fn [{:keys [db]} [_ category-models]]
      (let [query        (r viewer.queries/get-save-category-models-query          db category-models)
            validator-f #(r viewer.validators/save-category-models-response-valid? db %)]
           [:pathom/send-query! :vehicle-categories.viewer/save-category-models!
                                {:on-failure [:vehicle-categories.viewer/save-category-models-failed]
                                 :query query :validator-f validator-f}])))

(r/reg-event-fx :vehicle-categories.viewer/save-category-models-failed
  [:x.ui/render-bubble! ::notification
                        {:body :failed-to-save}])
