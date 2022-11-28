
(ns app.price-quotes.frontend.viewer.boxes
    (:require [app.common.frontend.api                  :as common]
              [app.components.frontend.api              :as components]
              [app.clients.frontend.api                 :as clients]
              [app.packages.frontend.api                :as packages]
              [app.price-quote-templates.frontend.api   :as price-quote-templates]
              [app.price-quotes.frontend.viewer.helpers :as viewer.helpers]
              [app.products.frontend.api                :as products]
              [app.services.frontend.api                :as services]
              [app.vehicle-models.frontend.api          :as vehicle-models]
              [app.vehicle-types.frontend.api           :as vehicle-types]
              [elements.api                             :as elements]
              [forms.api                                :as forms]
              [re-frame.api                             :as r]
              [time.api                                 :as time]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-release-date
  []
  (let [viewer-disabled?         @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        price-quote-release-date @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :release-date]])]
       [components/data-element ::price-quote-release-date
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :release-date
                                 :placeholder "n/a"
                                 :value       (time/timestamp-string->date price-quote-release-date)}]))

(defn- price-quote-validity-interval
  []
  (let [viewer-disabled?              @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        price-quote-validity-interval @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :validity-interval]])]
       [components/data-element ::price-quote-validity-interval
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :validity-interval
                                 :placeholder "n/a"
                                 :value       {:content :n-days :replacements [price-quote-validity-interval]}}]))

(defn- price-quote-validity-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [components/surface-box ::price-quote-validity-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-release-date]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-validity-interval]]
                                                    [:div (forms/form-block-attributes {:ratio 34})]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :validity}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-more-items-price
  []
  (let [viewer-disabled?         @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        vehicle-more-items-price @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :more-items-price]])]
       [components/data-element ::price-quote-more-items-price
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :more-items-price
                                 :placeholder "n/a"
                                 :value       {:content vehicle-more-items-price :suffix " EUR"}}]))

(defn- price-quote-vehicle-count
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        vehicle-count    @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-count]])]
       [components/data-element ::price-quote-vehicle-count
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :vehicle-count
                                 :placeholder "n/a"
                                 :value       vehicle-count}]))

(defn- price-quote-vehicle-unique-price
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        vehicle-unique-price @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-unique-price]])]
       [components/data-element ::price-quote-vehicle-unique-price
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :vehicle-unique-price
                                 :placeholder "n/a"
                                 :value       {:content vehicle-unique-price :suffix " EUR"}}]))

(defn- price-quote-vehicle-unit-price
  []
  (let [viewer-disabled?   @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        vehicle-unit-price @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-unit-price]])]
       [components/data-element ::price-quote-vehicle-unit-price
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :info-text   {:content :vehicle-unit-price-helper-n :replacements ["5"]}
                                 :label       :vehicle-unit-price
                                 :placeholder "n/a"
                                 :value       {:content vehicle-unit-price :suffix " EUR"}}]))

(defn- price-quote-total-vat
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        total-vat         (viewer.helpers/get-total-vat)]
       [components/data-element ::price-quote-total-vat
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :s :vertical :s}
                                 :label       :vat
                                 :placeholder "n/a"
                                 :value       {:content total-vat :suffix " EUR"}}]))

(defn- price-quote-net-total-price
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        net-total-price   (viewer.helpers/get-net-total-price)]
       [components/data-element ::price-quote-net-total-price
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :s :vertical :s}
                                 :label       :net-total-price
                                 :placeholder "n/a"
                                 :value       {:content net-total-price :suffix " EUR"}}]))

(defn- price-quote-gross-total-price
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        gross-total-price (viewer.helpers/get-gross-total-price)]
       [components/data-element ::price-quote-gross-total-price
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :s :vertical :s}
                                 :label       :gross-total-price
                                 :placeholder "n/a"
                                 :value       {:content gross-total-price :suffix " EUR"}}]))

(defn- price-quote-prices-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [components/surface-box ::price-quote-prices-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-vehicle-count]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-vehicle-unit-price]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          (if-let [vehicle-unique-pricing? @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-unique-pricing?]])]
                                                                  [price-quote-vehicle-unique-price]
                                                                  [price-quote-more-items-price])]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          (if-let [vehicle-unique-pricing? @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-unique-pricing?]])]
                                                                  [price-quote-more-items-price])]
                                                    [:div (forms/form-block-attributes {:ratio 67})]]
                                              [elements/horizontal-line {:color :highlight :indent {:top :m}}]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-net-total-price]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-total-vat]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [price-quote-gross-total-price]]
                                                    [:div (forms/form-block-attributes {:ratio 34})]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :prices}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-product-picker
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [products/product-picker ::price-quote-product-picker
                                {:autosave?     false
                                 :disabled?     viewer-disabled?
                                 :multi-select? true
                                 :placeholder   "n/a"
                                 :sortable?     true
                                 :value-path    [:price-quotes :viewer/viewed-item :products]

                                 ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
                                 ;:read-only? true
                                 :indent {:top :m :vertical :s}}]))
                                 ;:indent {:vertical :s}



(defn- price-quote-products-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [components/surface-box ::price-quote-products-box
                               {:content [:<> [price-quote-product-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :products}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-service-previews
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        price-quote-services @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :services]])]
       [services/service-preview ::price-quote-service-previews
                                 {:indent      {:top :m :vertical :s}
                                  :items       price-quote-services
                                  :placeholder "n/a"}]))

(defn- price-quote-services-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [components/surface-box ::price-quote-services-box
                               {:content [:<> [price-quote-service-previews]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :services}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-package-previews
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        price-quote-packages @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :packages]])]
       [packages/package-preview ::price-quote-package-previews
                                 {:indent      {:top :m :vertical :s}
                                  :items       price-quote-packages
                                  :placeholder "n/a"}]))

(defn- price-quote-packages-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [components/surface-box ::price-quote-packages-box
                               {:content [:<> [price-quote-package-previews]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :packages}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-template-preview
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        template-link    @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :template]])]
       [price-quote-templates/template-preview ::price-quote-template-preview
                                               {:disabled?   viewer-disabled?
                                                :item-link   template-link
                                                :indent      {:top :m :vertical :s}
                                                :placeholder "n/a"}]))

(defn- price-quote-template-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [components/surface-box ::price-quote-template-box
                               {:content [:<> [price-quote-template-preview]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :template}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-client-preview
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        client-link      @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :client]])]
       [clients/client-preview ::price-quote-client-preview
                               {:disabled?   viewer-disabled?
                                :item-link   client-link
                                :indent      {:top :m :vertical :s}
                                :placeholder "n/a"}]))

(defn- price-quote-client-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [components/surface-box ::price-quote-client-box
                               {:content [:<> [price-quote-client-preview]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :client}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-type-preview
  []
  (let [vehicle-count @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-count]])
        type-link     @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :type]])]
       [vehicle-types/type-preview ::price-quote-type-preview
                                   {:item-count  vehicle-count
                                    :item-link   type-link
                                    :indent      {:top :m :vertical :s}
                                    :placeholder "n/a"}]))

(defn- price-quote-type-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [components/surface-box ::price-quote-type-box
                               {:content [:<> [price-quote-type-preview]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :vehicle-type}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-model-preview
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        model-link       @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :model]])]
       [vehicle-models/model-preview ::price-quote-model-preview
                                     {:disabled?   viewer-disabled?
                                      :item-link   model-link
                                      :indent      {:top :m :vertical :s}
                                      :placeholder "n/a"}]))

(defn- price-quote-model-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [components/surface-box ::price-quote-model-box
                               {:content [:<> [price-quote-model-preview]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :vehicle-model}]))
