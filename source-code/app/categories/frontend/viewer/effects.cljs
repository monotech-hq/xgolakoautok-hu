
(ns app.categories.frontend.viewer.effects
    (:require [app.categories.frontend.viewer.queries    :as viewer.queries]
              [app.categories.frontend.viewer.validators :as viewer.validators]
              [app.categories.frontend.viewer.views      :as viewer.views]
              [re-frame.api                              :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :categories.viewer/load-viewer!
  ; @param (keyword) view-id
  (fn [_ [_ view-id]]
      {:dispatch-n [[:gestures/init-view-handler! :categories.viewer
                                                  {:default-view-id (or view-id :overview)}]
                    [:ui/render-surface! :categories.viewer/view
                                         {:content #'viewer.views/view}]]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :categories.viewer/save-category-models!
  (fn [{:keys [db]} [_ category-models]]
      (let [query        (r viewer.queries/get-save-category-models-query          db category-models)
            validator-f #(r viewer.validators/save-category-models-response-valid? db %)]
           [:pathom/send-query! :categories.viewer/save-category-models!
                                {:on-failure [:categories.viewer/save-category-models-failed]
                                 :query query :validator-f validator-f}])))

(r/reg-event-fx :categories.viewer/save-category-models-failed
  [:ui/render-bubble! ::notification
                      {:body :failed-to-save}])
