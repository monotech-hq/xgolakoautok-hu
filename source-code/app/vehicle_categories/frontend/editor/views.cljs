
(ns app.vehicle-categories.frontend.editor.views
    (:require [app.common.frontend.api                      :as common]
              [app.components.frontend.api                  :as components]
              [app.vehicle-categories.frontend.editor.boxes :as editor.boxes]
              [elements.api                                 :as elements]
              [layouts.surface-a.api                        :as surface-a]
              [re-frame.api                                 :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-editor-footer :vehicle-categories.editor {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-settings
  []
  [:<> [editor.boxes/category-settings-box]])

(defn- category-vehicle-models
  []
  [:<> [editor.boxes/category-vehicle-models-box]])

(defn- category-thumbnail
  []
  [:<> [editor.boxes/category-thumbnail-box]])

(defn- category-data
  []
  [:<> [editor.boxes/category-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :vehicle-categories.editor])]
       (case current-view-id :data           [category-data]
                             :thumbnail      [category-thumbnail]
                             :vehicle-models [category-vehicle-models]
                             :settings       [category-settings])))

(defn- body
  []
  [common/item-editor-body :vehicle-categories.editor
                           {:form-element     [view-selector]
                            :initial-item     {:visibility :public}
                            :item-path        [:vehicle-categories :editor/edited-item]
                            :suggestions-path [:vehicle-categories :editor/suggestions]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [category-name  @(r/subscribe [:x.db/get-item [:vehicle-categories :editor/edited-item :name]])
        category-id    @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        category-route @(r/subscribe [:item-editor/get-item-route :vehicle-categories.editor category-id])]
       [common/item-editor-header :vehicle-categories.editor
                                  {:label       category-name
                                   :placeholder :unnamed-vehicle-category
                                   :crumbs      [{:label :app-home           :route "/@app-home"}
                                                 {:label :vehicle-categories :route "/@app-home/vehicle-categories"}
                                                 {:label category-name       :route category-route :placeholder :unnamed-vehicle-category}]
                                   :menu-items  [{:label :data           :change-keys [:name :description]}
                                                 {:label :thumbnail      :change-keys [:thumbnail]}
                                                 {:label :vehicle-models :change-keys [:models]}
                                                 {:label :settings       :change-keys [:visibility]}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
