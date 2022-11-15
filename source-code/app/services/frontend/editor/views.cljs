
(ns app.services.frontend.editor.views
    (:require [app.common.frontend.api  :as common]
              [app.storage.frontend.api :as storage]
              [elements.api             :as elements]
              [engines.item-editor.api  :as item-editor]
              [forms.api                :as forms]
              [layouts.surface-a.api    :as surface-a]
              [re-frame.api             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-quantity-unit-select
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [elements/select ::service-quantity-unit-select
                        {:disabled?      editor-disabled?
                         :indent         {:top :m :vertical :s}
                         :option-label-f :label
                         :options        [{:label :unit   :value :n-units}
                                          {:label :minute :value :n-mins}
                                          {:label :hour   :value :n-hours}
                                          {:label :day    :value :n-days}]
                         :label          :quantity-unit
                         :value-path     [:services :editor/edited-item :quantity-unit]}]))

(defn- service-item-number-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [elements/text-field ::service-item-number-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :item-number
                             :placeholder "xxx-xxx"
                             :value-path  [:services :editor/edited-item :item-number]}]))

(defn- service-price-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [elements/text-field ::service-price-field
                            {:disabled?      editor-disabled?
                             :end-adornments [{:label "EUR" :color :muted}]
                             :indent         {:top :m :vertical :s}
                             :label          :unit-price
                             :placeholder    "0"
                             :value-path     [:services :editor/edited-item :unit-price]}]))

(defn- service-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [elements/combo-box ::service-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:services :editor/suggestions :name]
                            :placeholder  :service-name-placeholder
                            :required?    true
                            :value-path   [:services :editor/edited-item :name]}]))

(defn- service-description-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [elements/multiline-field ::service-description-field
                                 {:disabled?   editor-disabled?
                                  :indent      {:top :m :vertical :s}
                                  :label       :description
                                  :placeholder :service-description-placeholder
                                  :value-path  [:services :editor/edited-item :description]}]))

(defn- service-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [common/surface-box ::service-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 66})
                                                      [service-name-field]]
                                                [:div (forms/form-block-attributes {:ratio 34})]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [service-quantity-unit-select]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [service-price-field]]
                                                [:div (forms/form-block-attributes {:ratio 34})
                                                      [service-item-number-field]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [service-description-field]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-data
  []
  [:<> [service-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :services.editor])]
       (case current-view-id :data [service-data])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [common/item-editor-menu-bar :services.editor
                                    {:disabled?  editor-disabled?
                                     :menu-items [{:label :data :change-keys [:name :description :unit-price :quantity-unit :item-number]}]}]))

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [common/item-editor-controls :services.editor
                                    {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])
        service-name     @(r/subscribe [:x.db/get-item [:services :editor/edited-item :name]])
        service-id       @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        service-uri       (str "/@app-home/services/" service-id)]
       [common/surface-breadcrumbs :services.editor/view
                                   {:crumbs (if service-id [{:label :app-home    :route "/@app-home"}
                                                            {:label :services    :route "/@app-home/services"}
                                                            {:label service-name :route service-uri :placeholder :unnamed-service}
                                                            {:label :edit!}]
                                                           [{:label :app-home :route "/@app-home"}
                                                            {:label :services :route "/@app-home/services"}
                                                            {:label :add!}])
                                    :disabled? editor-disabled?}]))

(defn- label
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])
        service-name     @(r/subscribe [:x.db/get-item [:services :editor/edited-item :name]])]
       [common/surface-label :services.editor/view
                             {:disabled?   editor-disabled?
                              :label       service-name
                              :placeholder :unnamed-service}]))

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

(defn- service-editor
  []
  [item-editor/body :services.editor
                    {:auto-title?      true
                     :form-element     #'view-structure
                     :error-element    [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-editor-ghost-element
                     :initial-item     {:quantity-unit {:label :unit :value :n-units}}
                     :item-path        [:services :editor/edited-item]
                     :label-key        :name
                     :suggestion-keys  [:name]
                     :suggestions-path [:services :editor/suggestions]}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'service-editor}])
