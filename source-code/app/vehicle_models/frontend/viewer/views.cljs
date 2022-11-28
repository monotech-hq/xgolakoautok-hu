
(ns app.vehicle-models.frontend.viewer.views
    (:require [app.common.frontend.api                  :as common]
              [app.components.frontend.api              :as components]
              [app.vehicle-models.frontend.viewer.boxes :as viewer.boxes]
              [elements.api                             :as elements]
              [layouts.surface-a.api                    :as surface-a]
              [re-frame.api                             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-viewer-footer :vehicle-models.viewer {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-vehicle-types
  []
  [:<> [viewer.boxes/model-vehicle-types-box]])

(defn- model-overview
  []
  [:<> [viewer.boxes/model-data-box]
       [viewer.boxes/model-thumbnail-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :vehicle-models.viewer])]
       (case current-view-id :overview      [model-overview]
                             :vehicle-types [model-vehicle-types]
                             ; Még létezik a ".../types" útvonal!
                             :types [model-vehicle-types])))

(defn- body
  []
  [common/item-viewer-body :vehicle-models.viewer
                           {:item-element [view-selector]
                            :item-path    [:vehicle-models :viewer/viewed-item]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [model-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        model-name @(r/subscribe [:x.db/get-item [:vehicle-models :viewer/viewed-item :name]])
        edit-route  (str "/@app-home/vehicle-models/"model-id"/edit")]
       [common/item-viewer-header :vehicle-models.viewer
                                  {:label       model-name
                                   :placeholder :unnamed-vehicle-model
                                   :crumbs     [{:label :app-home       :route "/@app-home"}
                                                {:label :vehicle-models :route "/@app-home/vehicle-models"}
                                                {:label model-name :placeholder :unnamed-vehicle-model}]
                                   :menu-items [{:label :overview}
                                                {:label :vehicle-types}]
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
