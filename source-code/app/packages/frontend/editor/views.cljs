
(ns app.packages.frontend.editor.views
    (:require [app.common.frontend.api            :as common]
              [app.components.frontend.api        :as components]
              [app.packages.frontend.editor.boxes :as editor.boxes]
              [elements.api                       :as elements]
              [layouts.surface-a.api              :as surface-a]
              [forms.api                          :as forms]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-editor-footer :packages.editor {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-settings
  []
  [:<> [editor.boxes/package-settings-box]])

(defn- package-products
  []
  [:<> [editor.boxes/package-products-box]])

(defn- package-services
  []
  [:<> [editor.boxes/package-services-box]])

(defn- package-thumbnail
  []
  [:<> [editor.boxes/package-thumbnail-box]])

(defn- package-data
  []
  [:<> [editor.boxes/package-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :packages.editor])]
       (case current-view-id :data      [package-data]
                             :thumbnail [package-thumbnail]
                             :products  [package-products]
                             :services  [package-services]
                             :settings  [package-settings])))

(defn- body
  []
  [common/item-editor-body :packages.editor
                           {:form-element     [view-selector]
                            :initial-item     {:automatic-pricing? true :quantity-unit {:label :unit :value :n-units}}
                            :item-path        [:packages :editor/edited-item]
                            :suggestions-path [:packages :editor/suggestions]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [package-name  @(r/subscribe [:x.db/get-item [:packages :editor/edited-item :name]])
        package-id    @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        package-route @(r/subscribe [:item-editor/get-item-route :packages.editor package-id])]
       [common/item-editor-header :packages.editor
                                  {:label       package-name
                                   :placeholder :unnamed-package
                                   :crumbs      [{:label :app-home    :route "/@app-home"}
                                                 {:label :packages    :route "/@app-home/packages"}
                                                 {:label package-name :route package-route :placeholder :unnamed-package}]
                                   :menu-items  [{:label :data      :change-keys [:name :unit-price :description :quantity-unit :item-number]}
                                                 {:label :thumbnail :change-keys [:thumbnail]}
                                                 {:label :products  :change-keys [:products]}
                                                 {:label :services  :change-keys [:services]}
                                                 {:label :settings  :change-keys [:automatic-pricing?]}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
