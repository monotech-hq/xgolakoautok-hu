
(ns app.packages.frontend.viewer.boxes
    (:require [app.common.frontend.api              :as common]
              [app.components.frontend.api          :as components]
              [app.packages.frontend.viewer.queries :as viewer.queries]
              [app.products.frontend.api            :as products]
              [app.services.frontend.api            :as services]
              [app.storage.frontend.api             :as storage]
              [elements.api                         :as elements]
              [forms.api                            :as forms]
              [re-frame.api                         :as r]
              [x.components.api                     :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-service-picker
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])]
       [services/service-picker ::package-service-picker
                                {:autosave?     false
                                 :countable?    true
                                 :disabled?     viewer-disabled?
                                 :indent        {:top :m :vertical :s}
                                 :multi-select? true
                                 :placeholder   "n/a"
                                 :sortable?     true
                                 :value-path    [:packages :viewer/viewed-item :services]

                                 ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
                                 :read-only? true}]))

(defn- package-services-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])]
       [components/surface-box ::package-services-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [package-service-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :services}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-product-picker
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])]
       [products/product-picker ::package-product-picker
                                {:autosave?     false
                                 :countable?    true
                                 :disabled?     viewer-disabled?
                                 :indent        {:top :m :vertical :s}
                                 :multi-select? true
                                 :placeholder   "n/a"
                                 :sortable?     true
                                 :value-path    [:packages :viewer/viewed-item :products]

                                 ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
                                 :read-only? true}]))

(defn- package-products-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])]
       [components/surface-box ::package-products-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [package-product-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :products}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-thumbnail-picker
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])]
       [storage/media-picker ::package-thumbnail-picker
                             {:autosave?     true
                              :disabled?     viewer-disabled?
                              :indent        {:top :m :vertical :s}
                              :multi-select? false
                              :placeholder   "n/a"
                              :value-path    [:packages :viewer/viewed-item :thumbnail]

                              ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
                              :read-only? true}]))

(defn- package-thumbnail-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])]
       [components/surface-box ::package-thumbnail-box
                               {:indent  {:top :m}
                                :content [:<> [package-thumbnail-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-quantity-unit
  []
  (let [viewer-disabled?      @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-quantity-unit @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :quantity-unit :label]])]
       [components/data-element ::package-quantity-unit
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :quantity-unit
                                 :placeholder "n/a"
                                 :value       package-quantity-unit}]))

(defn- package-description
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-description @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :description]])]
       [components/data-element ::package-description
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :description
                                 :placeholder "n/a"
                                 :value       package-description}]))

(defn- package-item-number
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-item-number @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :item-number]])]
       [components/data-element ::package-item-number
                                {:copyable?   true
                                 :disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :item-number
                                 :marked?     true
                                 :placeholder "n/a"
                                 :value       package-item-number}]))

(defn- package-pricing
  []
  (let [viewer-disabled?           @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-automatic-pricing? @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :automatic-pricing?]])]
       [components/data-element ::package-pricing
                                {:disabled? viewer-disabled?
                                 :indent    {:top :m :vertical :s}
                                 :label     :pricing
                                 :value     (if package-automatic-pricing? :automatic-pricing :manual-pricing)}]))

(defn- package-automatic-price-querier
  []
  [x.components/querier ::package-automatic-price-querier
                        {:query (viewer.queries/request-automatic-price-query)}])

(defn- package-automatic-price
  []
  (let [viewer-disabled?        @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-automatic-price @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :automatic-price]])]
       [components/data-element ::package-automatic-price
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :unit-price
                                 :placeholder "n/a"
                                 :value       {:content package-automatic-price :suffix " EUR"}}]))

(defn- package-static-price
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-static-price @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :unit-price]])]
       [components/data-element ::package-static-price
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :unit-price
                                 :placeholder "n/a"
                                 :value       {:content package-static-price :suffix " EUR"}}]))

(defn- package-price
  []
  (if-let [automatic-pricing? @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :automatic-pricing?]])]
          [:<> [package-automatic-price-querier]
               [package-automatic-price]]
          [package-static-price]))

(defn- package-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-name     @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :name]])]
       [components/data-element ::package-name
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :name
                                 :placeholder "n/a"
                                 :value       package-name}]))

(defn- package-product-count
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-products     @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :products]])
        package-product-count (count package-products)]
       [components/data-element ::package-product-count
                                {:disabled? viewer-disabled?
                                 :indent    {:top :m :vertical :s}
                                 :label     :product-count
                                 :value     (str package-product-count)}]))

(defn- package-service-count
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-services     @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :services]])
        package-service-count (count package-services)]
       [components/data-element ::package-service-count
                                {:disabled? viewer-disabled?
                                 :indent    {:top :m :vertical :s}
                                 :label     :service-count
                                 :value     (str package-service-count)}]))

(defn- package-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])]
       [components/surface-box ::package-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [package-name]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [package-product-count]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [package-service-count]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [package-pricing]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [package-quantity-unit]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [package-price]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [package-item-number]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [package-description]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :label     :data
                                :disabled? viewer-disabled?}]))
