
(ns app.settings.frontend.editor.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]
              [x.locales.api               :as x.locales]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- settings-primary-currency-select
  []
  (let [editor-disabled?   @(r/subscribe [:item-editor/editor-disabled? :settings.editor])
        secondary-currency @(r/subscribe [:x.db/get-item [:settings :editor/edited-item :secondary-currency]])]
       [elements/select ::settings-primary-currency-select
                        {:disabled?  editor-disabled?
                         :indent     {:top :m :vertical :s}
                         :label      :primary-currency
                         :options    x.locales/CURRENCY-SYMBOLS
                         :required?  true
                         :value-path [:settings :editor/edited-item :primary-currency]}]))

(defn- settings-secondary-currency-select
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :settings.editor])
        primary-currency @(r/subscribe [:x.db/get-item [:settings :editor/edited-item :primary-currency]])]
       [elements/select ::settings-secondary-currency-select
                        {:disabled?  editor-disabled?
                         :indent     {:top :m :vertical :s}
                         :label      :secondary-currency
                         :options    x.locales/CURRENCY-SYMBOLS
                         :required?  true
                         :value-path [:settings :editor/edited-item :secondary-currency]}]))

(defn- settings-vat-value-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :settings.editor])]
       [elements/text-field ::settings-vat-value-field
                            {:disabled?      editor-disabled?
                             :end-adornments [{:label "%" :color :muted}]
                             :indent         {:top :m :vertical :s}
                             :label          :vat-value
                             :required?      true
                             :value-path     [:settings :editor/edited-item :vat-value]}]))

(defn- settings-current-price-field
  []
  (let [editor-disabled?   @(r/subscribe [:item-editor/editor-disabled? :settings.editor])
        primary-currency   @(r/subscribe [:x.db/get-item [:settings :editor/edited-item :primary-currency]])
        secondary-currency @(r/subscribe [:x.db/get-item [:settings :editor/edited-item :secondary-currency]])]
       [elements/text-field ::settings-current-price-field
                            {:disabled?      editor-disabled?
                             :end-adornments [{:label (str primary-currency" / " secondary-currency) :color :muted}]
                             :indent         {:top :m :vertical :s}
                             :label          :current-price
                             :required?      true
                             :value-path     [:settings :editor/edited-item :current-price]}]))

(defn- settings-taxes-and-current-price-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :settings.editor])]
       [components/surface-box ::taxes-and-current-price-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [settings-primary-currency-select]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [settings-secondary-currency-select]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [settings-current-price-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [settings-vat-value-field]]
                                                    [:div (forms/form-block-attributes {:ratio 33})]
                                                    [:div (forms/form-block-attributes {:ratio 34})]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :taxes-and-current-price}]))
