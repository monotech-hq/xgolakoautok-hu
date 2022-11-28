
(ns app.vehicle-types.frontend.viewer.views
    (:require [app.common.frontend.api                    :as common]
              [app.components.frontend.api                :as components]
              [app.schemes.frontend.api                   :as schemes]
              [app.vehicle-types.frontend.handler.queries :as handler.queries]
              [app.vehicle-types.frontend.viewer.boxes    :as viewer.boxes]
              [elements.api                               :as elements]
              [layouts.surface-a.api                      :as surface-a]
              [re-frame.api                               :as r]
              [vector.api                                 :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-viewer-footer :vehicle-types.viewer {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-technical-data
  []
  [:<> [viewer.boxes/type-technical-data-box]])

(defn- type-overview
  []
  [:<> [viewer.boxes/type-basic-data-box]
       [viewer.boxes/type-images-box]
       [viewer.boxes/type-files-box]
       [viewer.boxes/type-price-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :vehicle-types.viewer])]
       (case current-view-id :overview       [type-overview]
                             :technical-data [type-technical-data])))

(defn- body
  []
  [common/item-viewer-body :vehicle-types.viewer
                           {:item-element [view-selector]
                            :item-path    [:vehicle-types :viewer/viewed-item]
                            :query        (vector/concat-items (schemes/request-scheme-form-query :vehicle-types.technical-data)
                                                               (handler.queries/request-model-name-query))}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [type-id    @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        model-id   @(r/subscribe [:x.router/get-current-route-path-param :model-id])
        type-name  @(r/subscribe [:x.db/get-item [:vehicle-models :viewer/viewed-item :name]])
        model-name @(r/subscribe [:x.db/get-item [:vehicle-types :handler/model-item :name]])
        edit-route  (str "/@app-home/vehicle-models/"model-id"/types/"type-id"edit")
        model-route (str "/@app-home/vehicle-models/"model-id"/types")]
       [common/item-viewer-header :vehicle-types.viewer
                                  {:label       model-name
                                   :placeholder :unnamed-vehicle-model
                                   :crumbs     [{:label :app-home       :route "/@app-home"}
                                                {:label :vehicle-models :route "/@app-home/vehicle-models"}
                                                {:label model-name      :route model-route :placeholder :unnamed-vehicle-model}
                                                {:label type-name :placeholder :unnamed-vehicle-type}]
                                   :menu-items [{:label :overview}
                                                {:label :technical-data}]
                                   :on-edit    [:x.router/go-to! edit-route]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
