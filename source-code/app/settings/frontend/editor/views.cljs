
(ns app.settings.frontend.editor.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.item-editor.api :as item-editor]
              [forms.api               :as forms]
              [layouts.surface-a.api   :as surface-a]
              [re-frame.api            :as r]
              [x.app-locales.api       :as x.locales]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- settings-primary-currency-select
  []
  (let [editor-disabled?   @(r/subscribe [:item-editor/editor-disabled? :settings.editor])
        secondary-currency @(r/subscribe [:db/get-item [:settings :editor/edited-item :secondary-currency]])]
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
        primary-currency @(r/subscribe [:db/get-item [:settings :editor/edited-item :primary-currency]])]
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
                             :end-adornments [{:label "%"}]
                             :indent         {:top :m :vertical :s}
                             :label          :vat-value
                             :required?      true
                             :value-path     [:settings :editor/edited-item :vat-value]}]))

(defn- settings-current-price-field
  []
  (let [editor-disabled?   @(r/subscribe [:item-editor/editor-disabled? :settings.editor])
        primary-currency   @(r/subscribe [:db/get-item [:settings :editor/edited-item :primary-currency]])
        secondary-currency @(r/subscribe [:db/get-item [:settings :editor/edited-item :secondary-currency]])]
       [elements/text-field ::settings-current-price-field
                            {:disabled?      editor-disabled?
                             :end-adornments [{:label (str primary-currency" / " secondary-currency)}]
                             :indent         {:top :m :vertical :s}
                             :label          :current-price
                             :required?      true
                             :value-path     [:settings :editor/edited-item :current-price]}]))

(defn- settings-taxes-and-current-price-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :settings.editor])]
       [common/surface-box ::taxes-and-current-price-box
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
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :taxes-and-current-price}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- settings-sales
  []
  [:<> [settings-taxes-and-current-price-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:gestures/get-current-view-id :settings.editor])]
       (case current-view-id :sales [settings-sales])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :settings.editor])]
       [common/item-editor-menu-bar :settings.editor
                                    {:disabled?  editor-disabled?
                                     :menu-items [{:label :appearance    :disabled? true}
                                                  {:label :notifications :disabled? true}
                                                  {:label :privacy       :disabled? true}
                                                  {:label :sales         :change-keys []}]}]))

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :settings.editor])]
       [common/item-editor-controls :settings.editor
                                    {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :settings.editor])]
       [common/surface-breadcrumbs :settings.editor/view
                                   {:crumbs [{:label :app-home :route "/@app-home"}
                                             {:label :settings}]}]))

(defn- label
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :settings.editor])]
       [common/surface-label :settings.editor/view
                             {:disabled? editor-disabled?
                              :label     :settings}]))

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

(defn- settings-editor
  []
  [item-editor/body :settings.editor
                    {:form-element     #'view-structure
                     :error-element    [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-editor-ghost-element
                     :item-path        [:settings :editor/edited-item]
                     :on-saved         [:settings.editor/user-settings-saved]
                     :suggestion-keys  []
                     :suggestions-path [:settings :editor/suggestions]}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'settings-editor}])
