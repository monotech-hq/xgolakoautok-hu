
(ns app.products.frontend.viewer.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.storage.frontend.api    :as storage]
              [elements.api                :as elements]
              [engines.item-lister.api     :as item-lister]
              [engines.item-viewer.api     :as item-viewer]
              [forms.api                   :as forms]
              [layouts.surface-a.api       :as surface-a]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [data-received? @(r/subscribe [:item-viewer/data-received? :products.viewer])]
          [common/item-viewer-item-info :products.viewer {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-thumbnail-preview
  []
  (let [viewer-disabled?  @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])
        product-thumbnail @(r/subscribe [:x.db/get-item [:products :viewer/viewed-item :thumbnail]])]
       [storage/media-preview ::product-thumbnail-preview
                              {:disabled?   viewer-disabled?
                               :indent      {:top :m :vertical :s}
                               :items       [product-thumbnail]
                               :placeholder "-"
                               :thumbnail   {:height :3xl :width :5xl}}]))

(defn- product-thumbnail-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])]
       [common/surface-box ::product-thumbnail-box
                           {:content [:<> [product-thumbnail-preview]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-images-preview
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])
        product-images   @(r/subscribe [:x.db/get-item [:products :viewer/viewed-item :images]])]
       [storage/media-preview ::product-images-preview
                              {:disabled?   viewer-disabled?
                               :indent      {:top :m :vertical :s}
                               :items       product-images
                               :placeholder "-"
                               :thumbnail   {:height :3xl :width :5xl}}]))

(defn- product-images-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])]
       [common/surface-box ::product-images-box
                           {:content [:<> [product-images-preview]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :label     :images}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-quantity-unit
  []
  (let [viewer-disabled?      @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])
        product-quantity-unit @(r/subscribe [:x.db/get-item [:products :viewer/viewed-item :quantity-unit :label]])]
       [common/data-element ::product-quantity-unit
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :quantity-unit
                             :placeholder "-"
                             :value       product-quantity-unit}]))

(defn- product-description
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])
        product-description @(r/subscribe [:x.db/get-item [:products :viewer/viewed-item :description]])]
       [common/data-element ::product-description
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :description
                             :placeholder "-"
                             :value       product-description}]))

(defn- product-item-number
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])
        product-item-number @(r/subscribe [:x.db/get-item [:products :viewer/viewed-item :item-number]])]
       [common/data-element ::product-item-number
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :item-number
                             :placeholder "-"
                             :value       product-item-number}]))

(defn- product-price
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])
        product-price    @(r/subscribe [:x.db/get-item [:products :viewer/viewed-item :unit-price]])]
       [common/data-element ::product-price
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :unit-price
                             :placeholder "-"
                             :value       {:content product-price :suffix " EUR"}}]))

(defn- product-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])
        product-name     @(r/subscribe [:x.db/get-item [:products :viewer/viewed-item :name]])]
       [common/data-element ::product-name
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :name
                             :placeholder "-"
                             :value       product-name}]))

(defn- product-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])]
       [common/surface-box ::product-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [product-name]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [product-quantity-unit]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [product-price]]
                                                [:div (forms/form-block-attributes {:ratio 34})
                                                      [product-item-number]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [product-description]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-overview
  []
  [:<> [product-data-box]
       [product-thumbnail-box]
       [product-images-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :products.viewer])]
       (case current-view-id :overview [product-overview])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])]
       [common/item-viewer-menu-bar :products.viewer
                                    {:disabled?  viewer-disabled?
                                     :menu-items [{:label :overview}]}]))

(defn- controls
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])
        product-id       @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        edit-item-uri     (str "/@app-home/products/"product-id"/edit")]
       [common/item-viewer-controls :products.viewer
                                    {:disabled?      viewer-disabled?
                                     :edit-item-uri edit-item-uri}]))

(defn- breadcrumbs
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])
        product-name     @(r/subscribe [:x.db/get-item [:products :viewer/viewed-item :name]])]
       [components/surface-breadcrumbs ::breadcrumbs
                                       {:crumbs [{:label :app-home    :route "/@app-home"}
                                                 {:label :products    :route "/@app-home/products"}
                                                 {:label product-name :placeholder :unnamed-product}]
                                        :disabled? viewer-disabled?}]))

(defn- label
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])
        product-name     @(r/subscribe [:x.db/get-item [:products :viewer/viewed-item :name]])]
       [components/surface-label ::label
                                 {:disabled?   viewer-disabled?
                                  :label       product-name
                                  :placeholder :unnamed-product}]))

(defn- header
  []
  [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "48px"}}
             [:div [label]
                   [breadcrumbs]]
             [:div [controls]]]
       [elements/horizontal-separator {:size :xxl}]
       [menu-bar]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  []
  [:<> [header]
       [body]
       [footer]])

(defn- product-viewer
  []
  [item-viewer/body :products.viewer
                    {:auto-title?   true
                     :error-element [components/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element #'common/item-viewer-ghost-element
                     :item-element  #'view-structure
                     :item-path     [:products :viewer/viewed-item]
                     :label-key     :name}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'product-viewer}])
