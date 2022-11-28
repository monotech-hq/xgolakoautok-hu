
(ns app.rental-vehicles.frontend.editor.views
    (:require [app.common.frontend.api                   :as common]
              [app.components.frontend.api               :as components]
              [app.rental-vehicles.frontend.editor.boxes :as editor.boxes]
              [elements.api                              :as elements]
              [layouts.surface-a.api                     :as surface-a]
              [re-frame.api                              :as r]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-editor-footer :rental-vehicles.editor {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-settings
  []
  [:<> [editor.boxes/vehicle-settings-box]])

(defn- vehicle-content
  []
  [:<> [editor.boxes/vehicle-description-box]])

(defn- vehicle-images
  []
  [:<> [editor.boxes/vehicle-images-box]])

(defn- vehicle-thumbnail
  []
  [:<> [editor.boxes/vehicle-thumbnail-box]])

(defn- vehicle-data
  []
  [:<> [editor.boxes/vehicle-basic-data-box]
       [editor.boxes/vehicle-technical-data-box]])

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :rental-vehicles.editor])]
       (case current-view-id :data      [vehicle-data]
                             :content   [vehicle-content]
                             :thumbnail [vehicle-thumbnail]
                             :images    [vehicle-images]
                             :settings  [vehicle-settings])))

(defn- body
  []
  (let [server-year @(r/subscribe [:x.core/get-server-year])]
       [common/item-editor-body :rental-vehicles.editor
                                {:form-element     [view-selector]
                                 :initial-item     {:construction-year server-year
                                                    :number-of-bunks   0
                                                    :number-of-seats   0
                                                    :type              :alcove-rv
                                                    :visibility        :public}
                                 :item-path        [:rental-vehicles :editor/edited-item]
                                 :suggestions-path [:rental-vehicles :editor/suggestions]}]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- header
  []
  (let [vehicle-name  @(r/subscribe [:x.db/get-item [:rental-vehicles :editor/edited-item :name]])
        vehicle-id    @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        vehicle-route @(r/subscribe [:item-editor/get-item-route :rental-vehicles.editor vehicle-id])]
       [common/item-editor-header :rental-vehicles.editor
                                  {:label       vehicle-name
                                   :placeholder :unnamed-rental-vehicle
                                   :crumbs      [{:label :app-home        :route "/@app-home"}
                                                 {:label :rental-vehicles :route "/@app-home/rental-vehicles"}
                                                 {:label vehicle-name     :route vehicle-route :placeholder :unnamed-rental-vehicle}]
                                   :menu-items  [{:label :data      :change-keys [:name :type :construction-year
                                                                                  :number-of-bunks :number-of-seats]}
                                                 {:label :thumbnail :change-keys [:thumbnail]}
                                                 {:label :images    :change-keys [:images]}
                                                 {:label :content   :change-keys [:description]}
                                                 {:label :settings  :change-keys [:visibility]}]}]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
