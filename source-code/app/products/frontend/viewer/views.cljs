
(ns app.products.frontend.viewer.views
    (:require [app.common.frontend.api            :as common]
              [app.components.frontend.api        :as components]
              [app.products.frontend.viewer.boxes :as viewer.boxes]
              [elements.api                       :as elements]
              [layouts.surface-a.api              :as surface-a]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-viewer-footer :products.viewer {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-overview
  []
  [:<> [viewer.boxes/product-data-box]
       [viewer.boxes/product-thumbnail-box]
       [viewer.boxes/product-images-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :products.viewer])]
       (case current-view-id :overview [product-overview])))

(defn- body
  []
  [common/item-viewer-body :products.viewer
                           {:item-element [view-selector]
                            :item-path    [:products :viewer/viewed-item]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [product-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        product-name @(r/subscribe [:x.db/get-item [:products :viewer/viewed-item :name]])
        edit-route    (str "/@app-home/products/"product-id"/edit")]
       [common/item-viewer-header :products.viewer
                                  {:label       product-name
                                   :placeholder :unnamed-product
                                   :crumbs     [{:label :app-home :route "/@app-home"}
                                                {:label :products :route "/@app-home/products"}
                                                {:label product-name :placeholder :unnamed-product}]
                                   :menu-items [{:label :overview}]
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
