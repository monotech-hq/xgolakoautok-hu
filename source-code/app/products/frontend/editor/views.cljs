
(ns app.products.frontend.editor.views
    (:require [app.common.frontend.api            :as common]
              [app.components.frontend.api        :as components]
              [app.products.frontend.editor.boxes :as editor.boxes]
              [elements.api                       :as elements]
              [layouts.surface-a.api              :as surface-a]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-editor-footer :products.editor {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-images
  []
  [:<> [editor.boxes/product-images-box]])

(defn- product-thumbnail
  []
  [:<> [editor.boxes/product-thumbnail-box]])

(defn- product-data
  []
  [:<> [editor.boxes/product-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :products.editor])]
       (case current-view-id :data      [product-data]
                             :thumbnail [product-thumbnail]
                             :images    [product-images])))

(defn- body
  []
  [common/item-editor-body :products.editor
                           {:form-element     [view-selector]
                            :initial-item     {:quantity-unit {:label :piece :value :n-pieces}}
                            :item-path        [:products :editor/edited-item]
                            :suggestions-path [:products :editor/suggestions]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [product-name  @(r/subscribe [:x.db/get-item [:products :editor/edited-item :name]])
        product-id    @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        product-route @(r/subscribe [:item-editor/get-item-route :products.editor product-id])]
       [common/item-editor-header :products.editor
                                  {:label       product-name
                                   :placeholder :unnamed-product
                                   :crumbs      [{:label :app-home    :route "/@app-home"}
                                                 {:label :products    :route "/@app-home/products"}
                                                 {:label product-name :route product-route :placeholder :unnamed-product}]
                                   :menu-items  [{:label :data      :change-keys [:name :description :unit-price :quantity-unit :item-number]}
                                                 {:label :thumbnail :change-keys [:thumbnail]}
                                                 {:label :images    :change-keys [:images]}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
