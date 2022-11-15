
(ns app.price-quotes.frontend.editor.views
    (:require [app.clients.frontend.api                 :as clients]
              [app.common.frontend.api                  :as common]
              [app.contents.frontend.api                :as contents]
              [app.models.frontend.api                  :as models]
              [app.packages.frontend.api                :as packages]
              [app.price-quote-templates.frontend.api   :as price-quote-templates]
              [app.price-quotes.frontend.handler.state  :as handler.state]
              [app.price-quotes.frontend.editor.helpers :as editor.helpers]
              [app.price-quotes.frontend.editor.queries :as editor.queries]
              [app.price-quotes.mid.handler.config      :as handler.config]
              [app.products.frontend.api                :as products]
              [app.services.frontend.api                :as services]
              [app.types.frontend.api                   :as types]
              [elements.api                             :as elements]
              [engines.item-editor.api                  :as item-editor]
              [forms.api                                :as forms]
              [layouts.surface-a.api                    :as surface-a]
              [mixed.api                         :as mixed]
              [mid-fruits.string                        :as string]
              [mid-fruits.vector                        :as vector]
              [re-frame.api                             :as r]
              [x.app-components.api                     :as x.components]))

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
                        :selectable?      true}]))

(defn- download-preview-button
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [common/action-bar ::download-preview-button {:disabled? editor-disabled?
                                                     :label     :show-preview!
                                                     :on-click  [:price-quotes.editor/download-pdf!]}]))

(defn- price-quote-preview-box
  []
  (let [editor-disabled?     @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        price-quote-template @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :template]])]
       [common/surface-box ::price-quote-preview-box
                           {:content [:<> (if price-quote-template [download-preview-button]
                                                                   [select-price-quote-template-helper])
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :indent    {:bottom :m}
                            :label     :preview}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-preview
  []
  [:<> [price-quote-preview-box]])

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
                                               :placeholder   "-"
                                               :value-path    [:price-quotes :editor/edited-item :template]}]))

(defn- price-quote-template-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [common/surface-box ::price-quote-template-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [price-quote-template-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :price-quote-template}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-template
  []
  [:<> [price-quote-template-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-vehicle-unique-price
  []
  (let [editor-disabled?     @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        vehicle-unique-price @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :vehicle-unique-price]])]
       [common/data-element ::price-quote-vehicle-unique-price
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :vehicle-unique-price
                             :placeholder "-"
                             :value       {:content vehicle-unique-price :suffix " EUR"}}]))

(defn- price-quote-vehicle-unit-price
  []
  (let [editor-disabled?               @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        price-quote-vehicle-unit-price @(r/subscribe [:db/get-item [:price-quotes :editor/vehicle-unit-price]])]
       [common/data-element ::price-quote-vehicle-unit-price
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :info-text   {:content :vehicle-unit-price-helper-n :replacements ["5"]}
                             :label       :vehicle-unit-price
                             :placeholder "-"
                             :value       {:content price-quote-vehicle-unit-price :suffix " EUR"}}]))

(defn- price-quote-vehicle-count
  []
  (let [editor-disabled?          @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        price-quote-vehicle-count @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :vehicle-count]])]
       [common/data-element ::price-quote-vehicle-count
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :vehicle-count
                             :placeholder "-"
                             :value       price-quote-vehicle-count}]))

(defn- price-quote-more-items-price
  []
  (let [editor-disabled?             @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        price-quote-more-items-price @(r/subscribe [:db/get-item [:price-quotes :editor/more-items-price]])]
       [common/data-element ::price-quote-more-items-price
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :more-items-price
                             :placeholder "-"
                             :value       {:content price-quote-more-items-price :suffix " EUR"}}]))

(defn- price-quote-total-vat
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        total-vat         (editor.helpers/get-total-vat)]
       [common/data-element ::price-quote-total-vat
                            {:disabled?   editor-disabled?
                             :indent      {:top :s :vertical :s}
                             :label       :vat
                             :placeholder "-"
                             :value       {:content total-vat :suffix " EUR"}}]))

(defn- price-quote-net-total-price
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        net-total-price   (editor.helpers/get-net-total-price)]
       [common/data-element ::price-quote-net-total-price
                            {:disabled?   editor-disabled?
                             :indent      {:top :s :vertical :s}
                             :label       :net-total-price
                             :placeholder "-"
                             :value       {:content net-total-price :suffix " EUR"}}]))

(defn- price-quote-gross-total-price
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        gross-total-price (editor.helpers/get-gross-total-price)]
       [common/data-element ::price-quote-gross-total-price
                            {:disabled?   editor-disabled?
                             :indent      {:top :s :vertical :s}
                             :label       :gross-total-price
                             :placeholder "-"
                             :value       {:content gross-total-price :suffix " EUR"}}]))

(defn- price-quote-prices-querier
  []
  [x.components/querier ::price-quote-prices-querier
                        {:query (vector/concat-items (editor.queries/request-more-items-price-query)
                                                     (editor.queries/request-vehicle-unit-price-query))}])

(defn- price-quote-prices-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [common/surface-box ::price-quote-prices-box
                           {:content [:<> [price-quote-prices-querier]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [price-quote-vehicle-count]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [price-quote-vehicle-unit-price]]
                                                [:div (forms/form-block-attributes {:ratio 34})
                                                      (if-let [vehicle-unique-pricing? @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :vehicle-unique-pricing?]])]
                                                              [price-quote-vehicle-unique-price]
                                                              [price-quote-more-items-price])]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      (if-let [vehicle-unique-pricing? @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :vehicle-unique-pricing?]])]
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
                                          [elements/horizontal-separator {:size :s}]]
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
        vehicle-unique-pricing? @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :vehicle-unique-pricing?]])
        vehicle-unit-price      @(r/subscribe [:db/get-item [:price-quotes :editor/vehicle-unit-price]])]
       [elements/text-field ::price-quote-vehicle-unique-price-field
                            {:disabled?      (or editor-disabled? (not vehicle-unique-pricing?))
                             :end-adornments [{:label "EUR"}]
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
       [common/surface-box ::price-quote-unique-price-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [price-quote-vehicle-unique-pricing-checkbox]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [price-quote-vehicle-unique-price-field]]
                                                [:div (forms/form-block-attributes {:ratio 34})]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :indent    {:top :m}
                            :label     :unique-price}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-prices
  []
  [:<> [price-quote-prices-box]
       [price-quote-unique-price-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-service-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [services/service-picker ::price-quote-service-picker
                                {:autosave?     false
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? true
                                 :placeholder   "-"
                                 :value-path    [:price-quotes :editor/edited-item :services]}]))

(defn- price-quote-services-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [common/surface-box ::price-quote-services-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [price-quote-service-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
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
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? true
                                 :placeholder   "-"
                                 :value-path    [:price-quotes :editor/edited-item :products]}]))

(defn- price-quote-products-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [common/surface-box ::price-quote-products-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [price-quote-product-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :products}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-package-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [packages/package-picker ::price-quote-package-picker
                                {:autosave?     false
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? true
                                 :placeholder   "-"
                                 :value-path    [:price-quotes :editor/edited-item :packages]}]))

(defn- price-quote-packages-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [common/surface-box ::price-quote-packages-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [price-quote-package-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :indent    {:top :m}
                            :label     :packages}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-items
  []
  [:<> [price-quote-products-box]
       [price-quote-services-box]
       [price-quote-packages-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-manufacturer-price
  []
  (let [editor-disabled?        @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        type-manufacturer-price @(r/subscribe [:db/get-item [:types :preview/downloaded-items ::price-quote-type-picker :manufacturer-price]])]
       [common/data-element ::price-quote-manufacturer-price
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :manufacturer-price
                             :placeholder "-"
                             :value       {:content type-manufacturer-price :suffix " EUR"}}]))

(defn- price-quote-price-margin
  []
  (let [editor-disabled?  @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        type-price-margin @(r/subscribe [:db/get-item [:types :preview/downloaded-items ::price-quote-type-picker :price-margin]])]
       [common/data-element ::price-quote-price-margin
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :price-margin
                             :placeholder "-"
                             :value       {:content type-price-margin :suffix " %"}}]))

(defn- price-quote-dealer-rebate
  []
  (let [editor-disabled?   @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        type-dealer-rebate @(r/subscribe [:db/get-item [:types :preview/downloaded-items ::price-quote-type-picker :dealer-rebate]])]
       [common/data-element ::price-quote-dealer-rebate
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :dealer-rebate
                             :placeholder "-"
                             :value       {:content type-dealer-rebate :suffix " %"}}]))

(defn- price-quote-vehicle-name
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        model-name       @(r/subscribe [:db/get-item [:models :preview/downloaded-items ::price-quote-model-picker :name]])
        type-name        @(r/subscribe [:db/get-item [:types  :preview/downloaded-items ::price-quote-type-picker  :name]])]
       [common/data-element ::price-quote-vehicle-name
                            {:autosave?     true
                             :disabled?     editor-disabled?
                             :indent        {:top :m :vertical :s}
                             :label         :name
                             :multi-select? false
                             :placeholder   "-"
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
       [common/surface-box ::price-quote-vehicle-box
                           {:content [:<> [:<> [:div (forms/form-row-attributes)
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
                                                           [price-quote-vehicle-counter]]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :indent    {:bottom :m}
                            :label     :vehicle}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-type-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        model-link       @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :model]])]
       [types/type-picker ::price-quote-type-picker
                          {:autosave?     true
                           :disabled?     (or editor-disabled? (not model-link))
                           :indent        {:vertical :s}
                           :model-id      (:model/id model-link)
                           :multi-select? false
                           :placeholder   "-"
                           :value-path    [:price-quotes :editor/edited-item :type]}]))

(defn- price-quote-type-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [common/surface-box ::price-quote-type-box
                           {:indent  {:top :m}
                            :content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [price-quote-type-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :type}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-model-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [models/model-picker ::price-quote-model-picker
                            {:autosave?     true
                             :disabled?     editor-disabled?
                             :indent        {:vertical :s}
                             :multi-select? false
                             :on-change     [:db/remove-item! [:price-quotes :editor/edited-item :type]]
                             :placeholder    "-"
                             :value-path    [:price-quotes :editor/edited-item :model]}]))

(defn- price-quote-model-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [common/surface-box ::price-quote-model-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [price-quote-model-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :model}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-vehicle
  []
  (let [model-link @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :model]])
        type-link  @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :type]])]
       [:<> (if type-link  ^{:key ::price-quote-vehicle-box} [price-quote-vehicle-box])
            (if :always    ^{:key ::price-quote-model-box}   [price-quote-model-box])
            (if model-link ^{:key ::price-quote-type-box}    [price-quote-type-box])]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-client-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [clients/client-picker ::price-quote-client-picker
                              {:autosave?    true
                               :disabled?    editor-disabled?
                               :indent       {:vertical :s}
                               :multi-select? false
                               :placeholder  "-"
                               :value-path   [:price-quotes :editor/edited-item :client]}]))

(defn- price-quote-client-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [common/surface-box ::price-quote-client-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [price-quote-client-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :icon      :people
                            :label     :client}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-client
  []
  [:<> [price-quote-client-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-issue-year
  []
  (let [editor-disabled?       @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        price-quote-issue-year @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :issue-year]])]
       [common/data-element ::price-quote-issue-year
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :release-year
                             :placeholder "-"
                             :value       price-quote-issue-year}]))

(defn- price-quote-index
  []
  (let [editor-disabled?  @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        price-quote-index @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :index]])]
       [common/data-element ::price-quote-index
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :index
                             :placeholder "-"
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
       [common/surface-box ::price-quote-more-settings-box
                           {:indent  {:top :m}
                            :content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [price-quote-issue-year]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [price-quote-index]]
                                                [:div (forms/form-block-attributes {:ratio 34})
                                                      [price-quote-version-counter]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})]
                                                [:div (forms/form-block-attributes {:ratio 67})]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :icon      :more_horiz
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
                             :end-adornments [{:label :day-unit}]
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
       [common/surface-box ::price-quote-validity-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [price-quote-release-date-field]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [price-quote-validity-interval-field]]
                                                [:div (forms/form-block-attributes {:ratio 34})]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled?   editor-disabled?
                            :icon        :verified
                            :icon-family :material-icons-outlined
                            :label       :validity}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-settings
  []
  [:<> [price-quote-validity-box]
       [price-quote-more-settings-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:gestures/get-current-view-id :price-quotes.editor])]
       (case current-view-id :client   [price-quote-client]
                             :vehicle  [price-quote-vehicle]
                             :items    [price-quote-items]
                             :prices   [price-quote-prices]
                             :template [price-quote-template]
                             :settings [price-quote-settings]
                             :preview  [price-quote-preview])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [common/item-editor-menu-bar :price-quotes.editor
                                    {:disabled?  editor-disabled?
                                     :menu-items [{:label :template :change-keys [:template]}
                                                  {:label :client   :change-keys [:client]}
                                                  {:label :vehicle  :change-keys [:model :type]}
                                                  {:label :items    :change-keys [:packages :products :services]}
                                                  {:label :prices   :change-keys [:vehicle-unique-pricing? :vehicle-unique-price]}
                                                  {:label :settings :change-keys [:release-date :validity-interval :version]}
                                                  {:label :preview}]}]))

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [common/item-editor-controls :price-quotes.editor
                                    {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])
        price-quote-name @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :name]])
        price-quote-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        price-quote-uri   (str "/@app-home/price-quotes/" price-quote-id)]
       [common/surface-breadcrumbs :price-quotes.editor/view
                                   {:crumbs (if price-quote-id [{:label :app-home        :route "/@app-home"}
                                                                {:label :price-quotes    :route "/@app-home/price-quotes"}
                                                                {:label price-quote-name :route price-quote-uri}
                                                                {:label :edit!}]
                                                               [{:label :app-home        :route "/@app-home"}
                                                                {:label :price-quotes    :route "/@app-home/price-quotes"}
                                                                {:label :add!}])
                                    :disabled? editor-disabled?}]))

(defn- label
  []
  (let [price-quote-name @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :name]])
        editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [common/surface-label :price-quotes.editor/view
                             {:disabled?   editor-disabled?
                              :label       {:content :price-quote-n :replacements [price-quote-name]}
                              :placeholder :new-price-quote}]))

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

(defn- price-quote-editor
  [_ _]
  (let [initial-values {:release-date      (editor.helpers/initial-release-date) :version 1
                        :validity-interval handler.config/DEFAULT-VALIDITY-INTERVAL}]
       [item-editor/body :price-quotes.editor
                         {:auto-title?      true
                          :form-element     #'view-structure
                          :error-element    [common/error-content {:error :the-item-you-opened-may-be-broken}]
                          :ghost-element    #'common/item-editor-ghost-element
                          :initial-item     initial-values
                          :item-path        [:price-quotes :editor/edited-item]
                          :label-key        :name
                          :suggestion-keys  [:name]
                          :suggestions-path [:price-quotes :editor/suggestions]}]))

(defn- preloader
  [_]
  [x.components/querier ::preloader
                        {:content     #'price-quote-editor
                         :placeholder #'common/item-editor-ghost-element
                         :query       (editor.queries/request-server-date-query)}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'preloader}])
