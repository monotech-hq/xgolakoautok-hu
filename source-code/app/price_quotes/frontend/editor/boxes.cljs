
(ns app.price-quotes.frontend.editor.boxes
    (:require [app.clients.frontend.api                 :as clients]
              [app.common.frontend.api                  :as common]
              [app.components.frontend.api              :as components]
              [app.vehicle-models.frontend.api          :as vehicle-models]
              [app.packages.frontend.api                :as packages]
              [app.price-quote-templates.frontend.api   :as price-quote-templates]
              [app.price-quotes.frontend.editor.helpers :as editor.helpers]
              [app.price-quotes.frontend.editor.queries :as editor.queries]
              [app.products.frontend.api                :as products]
              [app.services.frontend.api                :as services]
              [app.vehicle-types.frontend.api           :as vehicle-types]
              [elements.api                             :as elements]
              [forms.api                                :as forms]
              [mixed.api                                :as mixed]
              [re-frame.api                             :as r]
              [string.api                               :as string]
              [vector.api                               :as vector]
              [x.components.api                         :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-price-quote-template-helper
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [elements/label ::select-price-quote-template-label
                       {:color            :highlight
                        :content          :select-price-quote-template-helper
                        :disabled?        editor-disabled?
                        :font-size        :xs
                        :horizontal-align :center
                        :line-height      :block
                        :selectable?      true}]))

(defn- download-preview-button
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [components/action-bar ::download-preview-button {:disabled? editor-disabled?
                                                         :label     :show-preview!
                                                         :on-click  [:price-quotes.editor/download-pdf!]}]))

(defn- price-quote-preview-box
  []
  (let [editor-disabled?     @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        price-quote-template @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :template]])]
       [components/surface-box ::price-quote-preview-box
                               {:content [:<> (if price-quote-template [download-preview-button]
                                                                       [select-price-quote-template-helper])
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :indent    {:bottom :m}
                                :label     :preview}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-template-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [price-quote-templates/template-picker ::price-quote-template-picker
                                              {:autosave?     true
                                               :disabled?     editor-disabled?
                                               :indent        {:vertical :s}
                                               :multi-select? false
                                               :placeholder   "n/a"
                                               :value-path    [:price-quotes :editor/edited-item :template]}]))

(defn- price-quote-template-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [components/surface-box ::price-quote-template-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [price-quote-template-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :price-quote-template}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-vehicle-unique-price
  []
  (let [editor-disabled?     @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        vehicle-unique-price @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :vehicle-unique-price]])]
       [components/data-element ::price-quote-vehicle-unique-price
                                {:disabled?   editor-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :vehicle-unique-price
                                 :placeholder "n/a"
                                 :value       {:content vehicle-unique-price :suffix " EUR"}}]))

(defn- price-quote-vehicle-unit-price
  []
  (let [editor-disabled?               @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        price-quote-vehicle-unit-price @(r/subscribe [:x.db/get-item [:price-quotes :editor/vehicle-unit-price]])]
       [components/data-element ::price-quote-vehicle-unit-price
                                {:disabled?   editor-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :info-text   {:content :vehicle-unit-price-helper-n :replacements ["5"]}
                                 :label       :vehicle-unit-price
                                 :placeholder "n/a"
                                 :value       {:content price-quote-vehicle-unit-price :suffix " EUR"}}]))

(defn- price-quote-vehicle-count
  []
  (let [editor-disabled?          @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        price-quote-vehicle-count @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :vehicle-count]])]
       [components/data-element ::price-quote-vehicle-count
                                {:disabled?   editor-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :vehicle-count
                                 :placeholder "n/a"
                                 :value       price-quote-vehicle-count}]))

(defn- price-quote-more-items-price
  []
  (let [editor-disabled?             @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        price-quote-more-items-price @(r/subscribe [:x.db/get-item [:price-quotes :editor/more-items-price]])]
       [components/data-element ::price-quote-more-items-price
                                {:disabled?   editor-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :more-items-price
                                 :placeholder "n/a"
                                 :value       {:content price-quote-more-items-price :suffix " EUR"}}]))

(defn- price-quote-total-vat
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        total-vat         (editor.helpers/get-total-vat)]
       [components/data-element ::price-quote-total-vat
                                {:disabled?   editor-disabled?
                                 :indent      {:top :s :vertical :s}
                                 :label       :vat
                                 :placeholder "n/a"
                                 :value       {:content total-vat :suffix " EUR"}}]))

(defn- price-quote-net-total-price
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        net-total-price   (editor.helpers/get-net-total-price)]
       [components/data-element ::price-quote-net-total-price
                                {:disabled?   editor-disabled?
                                 :indent      {:top :s :vertical :s}
                                 :label       :net-total-price
                                 :placeholder "n/a"
                                 :value       {:content net-total-price :suffix " EUR"}}]))

(defn- price-quote-gross-total-price
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        gross-total-price (editor.helpers/get-gross-total-price)]
       [components/data-element ::price-quote-gross-total-price
                                {:disabled?   editor-disabled?
                                 :indent      {:top :s :vertical :s}
                                 :label       :gross-total-price
                                 :placeholder "n/a"
                                 :value       {:content gross-total-price :suffix " EUR"}}]))

(defn- price-quote-prices-querier
  []
  [x.components/querier ::price-quote-prices-querier
                        {:query (vector/concat-items (editor.queries/request-more-items-price-query)
                                                     (editor.queries/request-vehicle-unit-price-query))}])

(defn- price-quote-prices-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [components/surface-box ::price-quote-prices-box
                               {:content [:<> [price-quote-prices-querier]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-vehicle-count]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-vehicle-unit-price]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          (if-let [vehicle-unique-pricing? @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :vehicle-unique-pricing?]])]
                                                                  [price-quote-vehicle-unique-price]
                                                                  [price-quote-more-items-price])]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          (if-let [vehicle-unique-pricing? @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :vehicle-unique-pricing?]])]
                                                                  [price-quote-more-items-price])]
                                                    [:div (forms/form-block-attributes {:ratio 67})]]
                                              [elements/horizontal-line {:color :highlight :indent {:top :m}}]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-net-total-price]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-total-vat]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [price-quote-gross-total-price]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :prices}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-vehicle-unique-pricing-checkbox
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [elements/checkbox ::price-quote-vehicle-unique-pricing-checkbox
                          {:disabled?       editor-disabled?
                           :indent          {:top :m :vertical :s}
                           :options         [{:label :unique-pricing :helper :vehicle-unique-pricing-helper :value true}]
                           :option-helper-f :helper
                           :option-label-f  :label
                           :option-value-f  :value
                           :value-path      [:price-quotes :editor/edited-item :vehicle-unique-pricing?]}]))

(defn- price-quote-vehicle-unique-price-field
  []
  (let [editor-disabled?        @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        vehicle-unique-pricing? @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :vehicle-unique-pricing?]])
        vehicle-unit-price      @(r/subscribe [:x.db/get-item [:price-quotes :editor/vehicle-unit-price]])]
       [elements/text-field ::price-quote-vehicle-unique-price-field
                            {:disabled?      (or editor-disabled? (not vehicle-unique-pricing?))
                             :end-adornments [{:label "EUR" :color :muted}]
                             :indent         {:top :m :vertical :s}
                             :label          :unique-price
                             :placeholder    vehicle-unit-price
                             :required?      vehicle-unique-pricing?
                             :value-path     [:price-quotes :editor/edited-item :vehicle-unique-price]
                             :validator      (if vehicle-unique-pricing? {:f               mixed/whole-number?
                                                                          :invalid-message :invalid-number})}]))

(defn- price-quote-unique-price-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [components/surface-box ::price-quote-unique-price-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-vehicle-unique-pricing-checkbox]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-vehicle-unique-price-field]]
                                                    [:div (forms/form-block-attributes {:ratio 34})]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :indent    {:top :m}
                                :label     :unique-price}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-service-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [services/service-picker ::price-quote-service-picker
                                {:autosave?     false
                                 :countable?    true
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? true
                                 :placeholder   "n/a"
                                 :sortable?     true
                                 :value-path    [:price-quotes :editor/edited-item :services]}]))

(defn- price-quote-services-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [components/surface-box ::price-quote-services-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [price-quote-service-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :indent    {:top :m}
                                :label     :services}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-product-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [products/product-picker ::price-quote-product-picker
                                {:autosave?     false
                                 :countable?    true
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? true
                                 :placeholder   "n/a"
                                 :sortable?     true
                                 :value-path    [:price-quotes :editor/edited-item :products]}]))

(defn- price-quote-products-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [components/surface-box ::price-quote-products-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [price-quote-product-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :products}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-package-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [packages/package-picker ::price-quote-package-picker
                                {:autosave?     false
                                 :countable?    true
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? true
                                 :placeholder   "n/a"
                                 :sortable?     true
                                 :value-path    [:price-quotes :editor/edited-item :packages]}]))

(defn- price-quote-packages-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [components/surface-box ::price-quote-packages-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [price-quote-package-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :indent    {:top :m}
                                :label     :packages}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-manufacturer-price
  []
  (let [editor-disabled?        @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        type-link               @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :type]])
        type-manufacturer-price @(r/subscribe [:x.db/get-item [:vehicle-types :preview/downloaded-items (:type/id type-link):manufacturer-price]])]
       [components/data-element ::price-quote-manufacturer-price
                                {:disabled?   editor-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :manufacturer-price
                                 :placeholder "n/a"
                                 :value       {:content type-manufacturer-price :suffix " EUR"}}]))

(defn- price-quote-price-margin
  []
  (let [editor-disabled?  @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        type-link         @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :type]])
        type-price-margin @(r/subscribe [:x.db/get-item [:vehicle-types :preview/downloaded-items (:type/id type-link) :price-margin]])]
       [components/data-element ::price-quote-price-margin
                                {:disabled?   editor-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :price-margin
                                 :placeholder "n/a"
                                 :value       {:content type-price-margin :suffix " %"}}]))

(defn- price-quote-dealer-rebate
  []
  (let [editor-disabled?   @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        type-link          @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :type]])
        type-dealer-rebate @(r/subscribe [:x.db/get-item [:vehicle-types :preview/downloaded-items (:type/id type-link) :dealer-rebate]])]
       [components/data-element ::price-quote-dealer-rebate
                                {:disabled?   editor-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :dealer-rebate
                                 :placeholder "n/a"
                                 :value       {:content type-dealer-rebate :suffix " %"}}]))

(defn- price-quote-vehicle-name
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        model-link       @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :model]])
        type-link        @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :type]])
        model-name       @(r/subscribe [:x.db/get-item [:vehicle-models :preview/downloaded-items (:model/id model-link) :name]])
        type-name        @(r/subscribe [:x.db/get-item [:vehicle-types  :preview/downloaded-items (:type/id  type-link)  :name]])]
       [components/data-element ::price-quote-vehicle-name
                                {:autosave?     true
                                 :disabled?     editor-disabled?
                                 :indent        {:top :m :vertical :s}
                                 :label         :name
                                 :multi-select? false
                                 :placeholder   "n/a"
                                 :value         (string/join [model-name type-name] " – " {:join-empty? false})}]))

(defn- price-quote-vehicle-counter
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [elements/counter ::price-quote-vehicle-counter
                         {:disabled?     editor-disabled?
                          :label         :count
                          :indent        {:top :m :vertical :s}
                          :initial-value 1
                          :min-value     1
                          :max-value     16
                          :value-path    [:price-quotes :editor/edited-item :vehicle-count]}]))

(defn- price-quote-vehicle-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [components/surface-box ::price-quote-vehicle-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-manufacturer-price]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-price-margin]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [price-quote-dealer-rebate]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 66})
                                                          [price-quote-vehicle-name]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [price-quote-vehicle-counter]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :indent    {:bottom :m}
                                :label     :vehicle}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-type-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        model-link       @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :model]])]
       [vehicle-types/type-picker ::price-quote-type-picker
                                  {:autosave?     true
                                   :disabled?     (or editor-disabled? (not model-link))
                                   :indent        {:vertical :s}
                                   :model-id      (:model/id model-link)
                                   :multi-select? false
                                   :placeholder   "n/a"
                                   :value-path    [:price-quotes :editor/edited-item :type]}]))

(defn- price-quote-vehicle-type-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [components/surface-box ::price-quote-vehicle-type-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [price-quote-type-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :indent    {:top :m}
                                :label     :vehicle-type}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-model-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [vehicle-models/model-picker ::price-quote-model-picker
                                    {:autosave?     true
                                     :disabled?     editor-disabled?
                                     :indent        {:vertical :s}
                                     :multi-select? false
                                     :on-change     [:x.db/remove-item! [:price-quotes :editor/edited-item :type]]
                                     :placeholder    "n/a"
                                     :value-path    [:price-quotes :editor/edited-item :model]}]))

(defn- price-quote-vehicle-model-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [components/surface-box ::price-quote-vehicle-model-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [price-quote-model-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :vehicle-model}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-vehicle
  []
  (let [model-link @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :model]])
        type-link  @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :type]])]
       [:<> (if type-link  ^{:key ::price-quote-vehicle-box} [price-quote-vehicle-box])
            (if :always    ^{:key ::price-quote-model-box}   [price-quote-vehicle-model-box])
            (if model-link ^{:key ::price-quote-type-box}    [price-quote-vehicle-type-box])]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-client-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [clients/client-picker ::price-quote-client-picker
                              {:autosave?     true
                               :disabled?     editor-disabled?
                               :indent        {:vertical :s}
                               :multi-select? false
                               :placeholder   "n/a"
                               :value-path    [:price-quotes :editor/edited-item :client]}]))

(defn- price-quote-client-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [components/surface-box ::price-quote-client-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [price-quote-client-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :icon      :people
                                :label     :client}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-issue-year
  []
  (let [editor-disabled?       @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        price-quote-issue-year @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :issue-year]])]
       [components/data-element ::price-quote-issue-year
                                {:disabled?   editor-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :release-year
                                 :placeholder "n/a"
                                 :value       price-quote-issue-year}]))

(defn- price-quote-index
  []
  (let [editor-disabled?  @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        price-quote-index @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :index]])]
       [components/data-element ::price-quote-index
                                {:disabled?   editor-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :index
                                 :placeholder "n/a"
                                 :value       price-quote-index}]))

(defn- price-quote-version-counter
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [elements/counter ::price-quote-version-counter
                         {:disabled?  editor-disabled?
                          :indent     {:top :m :vertical :s}
                          :label      :version
                          :min-value  1
                          :max-value  256
                          :value-path [:price-quotes :editor/edited-item :version]}]))

(defn- price-quote-more-settings-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [components/surface-box ::price-quote-more-settings-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-issue-year]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [price-quote-index]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [price-quote-version-counter]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})]
                                                    [:div (forms/form-block-attributes {:ratio 67})]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :indent    {:top :m}
                                :label     :more-settings}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-release-date-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [elements/date-field ::price-quote-release-date-field
                            {:disabled?  editor-disabled?
                             :indent     {:top :m :vertical :s}
                             :label      :release-date
                             :required?  true
                             :value-path [:price-quotes :editor/edited-item :release-date]
                             :date-from  (editor.helpers/release-date-from)
                             :date-to    (editor.helpers/release-date-to)}]))

(defn- price-quote-validity-interval-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [elements/text-field ::price-quote-validity-interval-field
                            {:disabled?  editor-disabled?
                             :end-adornments [{:label :day-unit :color :muted}]
                             :indent     {:top :m :vertical :s}
                             :label      :validity-interval
                             :required?  true
                             :value-path [:price-quotes :editor/edited-item :validity-interval]
                             :validator  {:f               mixed/whole-number?
                                          :invalid-message :invalid-number
                                          :prevalidate?    true}}]))

(defn- price-quote-validity-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [components/surface-box ::price-quote-validity-box
                               {:content   [:<> [:div (forms/form-row-attributes)
                                                      [:div (forms/form-block-attributes {:ratio 33})
                                                            [price-quote-release-date-field]]
                                                      [:div (forms/form-block-attributes {:ratio 33})
                                                            [price-quote-validity-interval-field]]
                                                      [:div (forms/form-block-attributes {:ratio 34})]]
                                                [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :validity}]))
