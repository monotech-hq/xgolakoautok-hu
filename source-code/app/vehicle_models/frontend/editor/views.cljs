
(ns app.vehicle-models.frontend.editor.views
    (:require [app.common.frontend.api                  :as common]
              [app.components.frontend.api              :as components]
              [app.vehicle-models.frontend.editor.boxes :as editor.boxes]
              [elements.api                             :as elements]
              [layouts.surface-a.api                    :as surface-a]
              [re-frame.api                             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; meta-keywords    = tags
; meta-description = description

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-editor-footer :vehicle-models.editor {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-vehicle-types
  []
  [:<> [editor.boxes/model-vehicle-types-box]])

(defn- model-thumbnail
  []
  [:<> [editor.boxes/model-thumbnail-box]])

(defn- model-data
  []
  [:<> [editor.boxes/model-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :vehicle-models.editor])]
       (case current-view-id :data          [model-data]
                             :thumbnail     [model-thumbnail]
                             :vehicle-types [model-vehicle-types])))

(defn- body
  []
  [common/item-editor-body :vehicle-models.editor
                           {:form-element     [view-selector]
                            :item-path        [:vehicle-models :editor/edited-item]
                            :suggestion-keys  [:name :product-description :tags]
                            :suggestions-path [:vehicle-models :editor/suggestions]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [model-name  @(r/subscribe [:x.db/get-item [:vehicle-models :editor/edited-item :name]])
        model-id    @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        model-route @(r/subscribe [:item-editor/get-item-route :vehicle-models.editor model-id])]
       [common/item-editor-header :vehicle-models.editor
                                  {:label       model-name
                                   :placeholder :unnamed-vehicle-model
                                   :crumbs      [{:label :app-home       :route "/@app-home"}
                                                 {:label :vehicle-models :route "/@app-home/vehicle-models"}
                                                 {:label model-name      :route model-route :placeholder :unnamed-vehicle-model}]
                                   :menu-items  [{:label :data      :change-keys [:name :product-description :tags :description]}
                                                 {:label :thumbnail :change-keys [:thumbnail]}
                                                 {:label :vehicle-types}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
