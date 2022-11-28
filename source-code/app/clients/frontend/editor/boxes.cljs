
(ns app.clients.frontend.editor.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]
              [string.api                  :as string]
              [x.locales.api               :as x.locales]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-last-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])
        name-order       @(r/subscribe [:x.locales/get-name-order])]
       [elements/text-field ::client-last-name-field
                            {:autofocus?  (= name-order :reversed)
                             :disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :last-name
                             :placeholder :last-name-placeholder
                             :required?   true
                             :value-path  [:clients :editor/edited-item :last-name]}]))

(defn- client-first-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])
        name-order       @(r/subscribe [:x.locales/get-name-order])]
       [elements/text-field ::client-first-name-field
                            {:autofocus?  (= name-order :normal)
                             :disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :first-name
                             :placeholder :first-name-placeholder
                             :required?   true
                             :value-path  [:clients :editor/edited-item :first-name]}]))

(defn- client-name-fields
  []
  [:div (forms/form-row-attributes)
        [x.locales/name-order [:div (forms/form-block-attributes {:ratio 50})
                                    [client-first-name-field]]
                              [:div (forms/form-block-attributes {:ratio 50})
                                    [client-last-name-field]]
                             @(r/subscribe [:x.locales/get-name-order])]])

(defn- client-phone-number-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [elements/text-field ::client-phone-number-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :phone-number
                             ; Ha le lennének tiltva bizonoyos karakterek, nem lenne egyértelmű a mező használata!
                             ;:modifier   forms/phone-number
                             :modifier   #(if-not (empty? %) (string/starts-with! % "+"))
                             :placeholder :phone-number-placeholder
                             :required?   true
                             :validator   {:f forms/phone-number? :invalid-message :invalid-phone-number}
                             :value-path  [:clients :editor/edited-item :phone-number]}]))

(defn- client-email-address-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [elements/text-field ::client-email-address-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :email-address
                             :placeholder :email-address-placeholder
                             :required?   true
                             :validator   {:f forms/email-address? :invalid-message :invalid-email-address}
                             :value-path  [:clients :editor/edited-item :email-address]}]))

(defn- client-primary-contacts
  []
  [:div (forms/form-row-attributes)
        [:div (forms/form-block-attributes {:ratio 50})
              [client-email-address-field]]
        [:div (forms/form-block-attributes {:ratio 50})
              [client-phone-number-field]]])

(defn- client-basic-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [components/surface-box ::client-basic-data-box
                               {:content [:<> [client-name-fields]
                                              [client-primary-contacts]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :basic-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-company-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [elements/text-field ::client-company-name-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :company-name
                             :placeholder :company-name-placeholder
                             :value-path  [:clients :editor/edited-item :company-name]}]))

(defn- client-company-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [components/surface-box ::client-company-data-box
                               {:indent  {:top :m}
                                :content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 50})
                                                          [client-company-name-field]]
                                                    [:div (forms/form-block-attributes {:ratio 50})]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :company-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-vat-no-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [elements/text-field ::client-vat-no-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :vat-no
                             :placeholder :vat-no-placeholder
                             :value-path  [:clients :editor/edited-item :vat-no]}]))

(defn- client-country-select
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [elements/select ::client-country-select
                        {:disabled?  editor-disabled?
                         :indent     {:top :m :vertical :s}
                         :label      :country
                         :options    x.locales/EU-COUNTRY-NAMES
                         :value-path [:clients :editor/edited-item :country]}]))

(defn- client-zip-code-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [elements/combo-box ::client-zip-code-field
                           {:disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :zip-code
                            :options-path [:clients :editor/suggestions :zip-code]
                            :placeholder  :zip-code-placeholder
                            :value-path   [:clients :editor/edited-item :zip-code]}]))

(defn- client-city-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [elements/combo-box ::client-city-field
                           {:disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :city
                            :options-path [:clients :editor/suggestions :city]
                            :placeholder  :city-placeholder
                            :value-path   [:clients :editor/edited-item :city]}]))

(defn- client-address-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [elements/text-field ::client-address-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :address
                             :placeholder :address-placeholder
                             :value-path  [:clients :editor/edited-item :address]}]))


(defn- client-billing-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [components/surface-box ::client-billing-data-box
                               {:indent  {:top :m}
                                :content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 30})
                                                          [client-country-select]]
                                                    [:div (forms/form-block-attributes {:ratio 30})
                                                          [client-zip-code-field]]
                                                    [:div (forms/form-block-attributes {:ratio 40})
                                                          [client-city-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 60})
                                                          [client-address-field]]
                                                    [:div (forms/form-block-attributes {:ratio 40})
                                                          [client-vat-no-field]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :billing-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-color-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [components/color-picker ::client-color-picker
                                {:disabled?  editor-disabled?
                                 :indent     {:top :m :vertical :s}
                                 :label      :color
                                 :value-path [:clients :editor/edited-item :colors]}]))

(defn- client-tags-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [elements/multi-combo-box ::client-tags-field
                                 {:disabled?    editor-disabled?
                                  :indent       {:top :m :vertical :s}
                                  :label        :tags
                                  :options-path [:clients :editor/suggestions :tags]
                                  :placeholder  :client-tags-placeholder
                                  :value-path   [:clients :editor/edited-item :tags]}]))

(defn- client-description-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [elements/multiline-field ::client-description-field
                                 {:disabled?   editor-disabled?
                                  :indent      {:top :m :vertical :s}
                                  :label       :description
                                  :placeholder :client-description-placeholder
                                  :value-path  [:clients :editor/edited-item :description]}]))

(defn- client-more-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [components/surface-box ::client-more-data-box
                               {:indent  {:top :m}
                                :content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [client-color-picker]]
                                                    [:div (forms/form-block-attributes {:ratio 67})
                                                          [client-tags-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [client-description-field]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :more-data}]))
