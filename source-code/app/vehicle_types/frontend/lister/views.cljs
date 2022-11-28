
(ns app.vehicle-types.frontend.lister.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-list-item
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ; @param (integer) item-dex
  ; @param (map) type-item
  [_ _ item-dex {:keys [id modified-at name thumbnail]}]
  (let [timestamp @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        model-id  @(r/subscribe [:x.router/get-current-route-path-param :item-id])]
       [components/item-list-row {:cells [[components/list-item-thumbnail {:icon :article :icon-family :material-icons-outlined}]
                                          [components/list-item-cell      {:rows [{:content name :placeholder :unnamed-vehicle-type}]}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-cell      {:rows [{:content timestamp :font-size :xs :color :muted}] :width 100}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-button    {:label :open! :width 100 :on-click [:x.router/go-to! (str "/@app-home/vehicle-models/"model-id"/types/"id)]}]
                                          [components/list-item-gap       {:width 12}]]
                                  :border (if (not= item-dex 0) :top)}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-list-header
  []
  (let [current-order-by @(r/subscribe [:item-lister/get-current-order-by :vehicle-types.lister])]
       [components/item-list-header ::type-list-header
                                    {:cells [{:width 84}
                                             {:label :name :order-by-key :name
                                              :on-click [:item-lister/order-items! :vehicle-types.lister :name]}
                                             {:width 12}
                                             {:label :modified :width 100 :order-by-key :modified-at
                                              :on-click [:item-lister/order-items! :vehicle-types.lister :modified-at]}
                                             {:width 12}
                                             {:width 100}
                                             {:width 12}]
                                     :border :bottom
                                     :order-by current-order-by}]))

(defn- type-lister
  []
  (let [model-types @(r/subscribe [:x.db/get-item [:vehicle-models :viewer/viewed-item :types]])]
       [common/item-lister-body :vehicle-types.lister
                                {:display-progress? false
                                 :list-item-element #'type-list-item
                                 :item-list-header  #'type-list-header
                                 :items-path        [:vehicle-types :lister/downloaded-items]
                                 :prefilter         {:$or model-types}}]))
