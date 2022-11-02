
(ns app.types.frontend.viewer.views
    (:require [app.common.frontend.api            :as common]
              [app.schemes.frontend.api           :as schemes]
              [app.storage.frontend.api           :as storage]
              [app.types.frontend.handler.queries :as handler.queries]
              [elements.api                       :as elements]
              [engines.item-viewer.api            :as item-viewer]
              [forms.api                          :as forms]
              [mid-fruits.string                  :as string]
              [mid-fruits.vector                  :as vector]
              [layouts.surface-a.api              :as surface-a]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-scheme-field
  [field-id]
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])
        field-props      @(r/subscribe [:schemes.form-handler/get-scheme-field :types.technical-data field-id])
        field-value      @(r/subscribe [:db/get-item [:types :viewer/viewed-item field-id]])]
       [common/data-element {:indent      {:top :m :vertical :s}
                             :label       (:field/name field-props)
                             :placeholder "-"
                             :value       {:content field-value :suffix (str " " (:field/unit field-props))}}]))

(defn- type-scheme-form
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])
        scheme-field-ids @(r/subscribe [:schemes.form-handler/get-scheme-field-ids :types.technical-data])]
       (if (vector/nonempty? scheme-field-ids)
           (letfn [(f [field-list field-id] (conj field-list [type-scheme-field field-id]))]
                  (reduce f [:<>] scheme-field-ids))
           [elements/label ::no-scheme-field-label
                           {:color            :highlight
                            :content          :no-field-created
                            :disabled?        viewer-disabled?
                            :font-size        :xs
                            :horizontal-align :center}])))

(defn- type-technical-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])]
       [common/surface-box ::type-technical-data-box
                           {:content [:<> [type-scheme-form]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :technical-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-technical-data
  []
  [:<> [type-technical-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-images-preview
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])
        type-images      @(r/subscribe [:db/get-item [:types :viewer/viewed-item :images]])]
       [storage/media-preview ::type-images-preview
                              {:disabled?   viewer-disabled?
                               :indent      {:vertical :s}
                               :items       type-images
                               :placeholder "-"}]))

(defn- type-images-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])]
       [common/surface-box ::type-images-box
                           {:content [:<> [type-images-preview]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :label     :images}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-transport-cost
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])
        type-transport-cost @(r/subscribe [:db/get-item [:types :viewer/viewed-item :transport-cost]])]
       [common/data-element ::type-transport-cost
                            {:indent      {:top :m :vertical :s}
                             :label       :transport-cost
                             :placeholder "-"
                             :value       {:content type-transport-cost :suffix " EUR"}}]))

(defn- type-dealer-rebate
  []
  (let [viewer-disabled?   @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])
        type-dealer-rebate @(r/subscribe [:db/get-item [:types :viewer/viewed-item :dealer-rebate]])]
       [common/data-element ::type-dealer-rebate
                            {:indent      {:top :m :vertical :s}
                             :label       :dealer-rebate
                             :placeholder "-"
                             :value       {:content type-dealer-rebate :suffix " %"}}]))

(defn- type-price-margin
  []
  (let [viewer-disabled?  @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])
        type-price-margin @(r/subscribe [:db/get-item [:types :viewer/viewed-item :price-margin]])]
       [common/data-element ::type-price-margin
                            {:indent      {:top :m :vertical :s}
                             :label       :price-margin
                             :placeholder "-"
                             :value       {:content type-price-margin :suffix " %"}}]))

(defn- type-manufacturer-price
  []
  (let [viewer-disabled?        @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])
        type-manufacturer-price @(r/subscribe [:db/get-item [:types :viewer/viewed-item :manufacturer-price]])]
       [common/data-element ::type-manufacturer-price
                            {:indent      {:top :m :vertical :s}
                             :label       :manufacturer-price
                             :placeholder "-"
                             :value       {:content type-manufacturer-price :suffix " EUR"}}]))

(defn- type-price-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])]
       [common/surface-box ::type-price-box
                           {:indent  {:top :m}
                            :content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [type-manufacturer-price]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [type-price-margin]]
                                                [:div (forms/form-block-attributes {:ratio 34})
                                                      [type-dealer-rebate]]
                                           [:div (forms/form-row-attributes)
                                                 [:div (forms/form-block-attributes {:ratio 33})
                                                       [type-transport-cost]]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :price}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])
        type-name        @(r/subscribe [:db/get-item [:types :viewer/viewed-item :name]])]
       [common/data-element ::type-name
                            {:indent      {:top :m :vertical :s}
                             :label       :name
                             :placeholder "-"
                             :value       type-name}]))

(defn- type-basic-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])]
       [common/surface-box ::type-basic-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [type-name]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :basic-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-overview
  []
  [:<> [type-basic-data-box]
       [type-images-box]
       [type-price-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:gestures/get-current-view-id :types.viewer])]
       (case current-view-id :overview       [type-overview]
                             :technical-data [type-technical-data])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])]
       [common/item-viewer-menu-bar :types.viewer
                                    {:disabled?  viewer-disabled?
                                     :menu-items [{:label :overview}
                                                  {:label :technical-data}]}]))

(defn- controls
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])
        type-id          @(r/subscribe [:router/get-current-route-path-param :item-id])
        model-id         @(r/subscribe [:router/get-current-route-path-param :model-id])
        edit-item-uri     (str "/@app-home/models/"model-id"/types/"type-id"/edit")]
       [common/item-viewer-controls :types.viewer
                                    {:disabled?     viewer-disabled?
                                     :edit-item-uri edit-item-uri}]))

(defn- breadcrumbs
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])
        type-name        @(r/subscribe [:db/get-item [:types :viewer/viewed-item :name]])
        model-name       @(r/subscribe [:db/get-item [:types :handler/model-item :name]])
        model-id         @(r/subscribe [:router/get-current-route-path-param :model-id])
        model-uri         (str "/@app-home/models/" model-id "/types")]
       [common/surface-breadcrumbs :types.viewer/view
                                   {:crumbs [{:label :app-home  :route "/@app-home"}
                                             {:label :models    :route "/@app-home/models"}
                                             {:label model-name :route model-uri :placeholder :unnamed-model}
                                             {:label type-name  :placeholder :unnamed-type}]
                                    :disabled? viewer-disabled?}]))

(defn- label
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :types.viewer])
        type-name        @(r/subscribe [:db/get-item [:types :viewer/viewed-item :name]])]
       [common/surface-label :types.viewer/view
                             {:disabled?   viewer-disabled?
                              :label       type-name
                              :placeholder :unnamed-type}]))

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
       [body]])

(defn- type-viewer
  []
  [item-viewer/body :types.viewer
                    {:auto-title?   true
                     :error-element [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element #'common/item-viewer-ghost-element
                     :item-element  #'view-structure
                     :item-path     [:types :viewer/viewed-item]
                     :label-key     :name
                     :query         (vector/concat-items (schemes/request-scheme-form-query :types.technical-data)
                                                         (handler.queries/request-model-name-query))}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'type-viewer}])
