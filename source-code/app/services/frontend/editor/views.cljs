
(ns app.services.frontend.editor.views
    (:require [app.common.frontend.api            :as common]
              [app.components.frontend.api        :as components]
              [app.services.frontend.editor.boxes :as editor.boxes]
              [elements.api                       :as elements]
              [layouts.surface-a.api              :as surface-a]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-editor-footer :services.editor {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-thumbnail
  []
  [:<> [editor.boxes/service-thumbnail-box]])

(defn- service-data
  []
  [:<> [editor.boxes/service-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :services.editor])]
       (case current-view-id :data      [service-data]
                             :thumbnail [service-thumbnail])))

(defn- body
  []
  [common/item-editor-body :services.editor
                           {:form-element     [view-selector]
                            :initial-item     {:quantity-unit {:label :unit :value :n-units}}
                            :item-path        [:services :editor/edited-item]
                            :suggestions-path [:services :editor/suggestions]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [service-name  @(r/subscribe [:x.db/get-item [:services :editor/edited-item :name]])
        service-id    @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        service-route @(r/subscribe [:item-editor/get-item-route :services.editor service-id])]
       [common/item-editor-header :services.editor
                                  {:label       service-name
                                   :placeholder :unnamed-service
                                   :crumbs      [{:label :app-home    :route "/@app-home"}
                                                 {:label :services    :route "/@app-home/services"}
                                                 {:label service-name :route service-route :placeholder :unnamed-service}]
                                   :menu-items  [{:label :data :change-keys [:name :description :unit-price :quantity-unit :item-number]}
                                                 {:label :thumbnail :change-keys [:thumbnail]}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
