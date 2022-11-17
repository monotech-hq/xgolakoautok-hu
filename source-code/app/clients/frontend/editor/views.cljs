
(ns app.clients.frontend.editor.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.item-editor.api :as item-editor]
              [forms.api               :as forms]
              [layouts.surface-a.api   :as surface-a]
              [string.api       :as string]
              [re-frame.api            :as r :refer [r]]
              [x.locales.api           :as x.locales]))

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
       [common/surface-box ::client-basic-data-box
                           {:content [:<> [client-name-fields]
                                          [client-primary-contacts]
                                          [elements/horizontal-separator {:size :s}]]
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
       [common/surface-box ::client-company-data-box
                           {:indent  {:top :m}
                            :content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 50})
                                                      [client-company-name-field]]
                                                [:div (forms/form-block-attributes {:ratio 50})]]
                                          [elements/horizontal-separator {:size :s}]]
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
       [common/surface-box ::client-billing-data-box
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
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :billing-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-color-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [common/item-editor-color-picker :clients.editor
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
       [common/surface-box ::client-more-data-box
                           {:indent  {:top :m}
                            :content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [client-color-picker]]
                                                [:div (forms/form-block-attributes {:ratio 67})
                                                      [client-tags-field]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [client-description-field]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :more-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-data
  []
  [:<> [client-basic-data-box]
       [client-company-data-box]
       [client-billing-data-box]
       [client-more-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :clients.editor])]
       (case current-view-id :data [client-data])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [common/item-editor-menu-bar :clients.editor
                                    {:disabled?  editor-disabled?
                                     :menu-items [{:label :data :change-keys [:first-name :last-name :email-address
                                                                              :phone-number :country :zip-code :city
                                                                              :address :vat-no :description :colors
                                                                              :tags :company-name]}]}]))

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])]
       [common/item-editor-controls :clients.editor
                                    {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])
        client-name      @(r/subscribe [:clients.editor/get-client-name])
        client-id        @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        client-uri        (str "/@app-home/clients/" client-id)]
       [common/surface-breadcrumbs :clients.editor/view
                                   {:crumbs (if client-id [{:label :app-home   :route "/@app-home"}
                                                           {:label :clients    :route "/@app-home/clients"}
                                                           {:label client-name :route client-uri :placeholder :unnamed-client}
                                                           {:label :edit!}]
                                                          [{:label :app-home   :route "/@app-home"}
                                                           {:label :clients    :route "/@app-home/clients"}
                                                           {:label :add!}])
                                    :disabled? editor-disabled?}]))

(defn- label
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :clients.editor])
        client-name      @(r/subscribe [:clients.editor/get-client-name])
        client-colors    @(r/subscribe [:x.db/get-item [:clients :editor/edited-item :colors]])]
       [common/surface-label :clients.editor/view
                             {:disabled?   editor-disabled?
                              :label       client-name
                              :placeholder :unnamed-client}]))

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

(defn- client-editor
  []
  (let [user-locale      @(r/subscribe [:x.user/get-user-locale])
        user-country-name (get-in x.locales/COUNTRY-LIST [user-locale :native])]
       [item-editor/body :clients.editor
                         {:auto-title?      true
                          :form-element     #'view-structure
                          :error-element    [common/error-content {:error :the-item-you-opened-may-be-broken}]
                          :ghost-element    #'common/item-editor-ghost-element
                          :initial-item     {:country user-country-name}
                          :item-path        [:clients :editor/edited-item]
                          :label-key        :name
                          :suggestion-keys  [:city :zip-code :tags]
                          :suggestions-path [:clients :editor/suggestions]}]))

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'client-editor}])
