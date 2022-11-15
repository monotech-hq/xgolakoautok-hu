
(ns app.packages.frontend.editor.views
    (:require [app.common.frontend.api  :as common]
              [app.storage.frontend.api :as storage]
              [elements.api             :as elements]
              [engines.item-editor.api  :as item-editor]
              [engines.item-lister.api  :as item-lister]
              [forms.api                :as forms]
              [layouts.surface-a.api    :as surface-a]
              [re-frame.api             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-automatic-pricing-checkbox
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [elements/checkbox ::package-automatic-pricing-checkbox
                          {:disabled?       editor-disabled?
                           :indent          {:top :m :vertical :s}
                           :options         [{:label :automatic-pricing :helper :automatic-pricing-helper :value true}]
                           :option-helper-f :helper
                           :option-label-f  :label
                           :option-value-f  :value
                           :value-path      [:packages :editor/edited-item :automatic-pricing?]}]))

(defn- package-settings-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [common/surface-box ::package-settings-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [package-automatic-pricing-checkbox]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :settings}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-settings
  []
  [:<> [package-settings-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-thumbnail-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [storage/media-picker ::package-thumbnail-picker
                             {:autosave?     true
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? false
                              :placeholder   "-"
                              :toggle-label  :select-image!
                              :thumbnail     {:height :3xl :width :5xl}
                              :value-path    [:packages :editor/edited-item :thumbnail]}]))

(defn- package-thumbnail-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [common/surface-box ::package-thumbnail-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [package-thumbnail-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-thumbnail
  []
  [:<> [package-thumbnail-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-quantity-unit-select
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [elements/select ::package-quantity-unit-select
                        {:disabled?      editor-disabled?
                         :indent         {:top :m :vertical :s}
                         :option-label-f :label
                         :options        [{:label :unit       :value :n-units}
                                          {:label :piece      :value :n-pieces}
                                          {:label :minute     :value :n-mins}
                                          {:label :hour       :value :n-hours}
                                          {:label :day        :value :n-days}
                                          {:label :millimeter :value :n-mm}
                                          {:label :centimeter :value :n-cm}
                                          {:label :decimeter  :value :n-dm}
                                          {:label :meter      :value :n-m}
                                          {:label :kilometer  :value :n-km}
                                          {:label :milligram  :value :n-mg}
                                          {:label :gram       :value :n-gr}
                                          {:label :kilogram   :value :n-kg}
                                          {:label :ton        :value :n-t}]
                         :label          :quantity-unit
                         :value-path     [:packages :editor/edited-item :quantity-unit]}]))

(defn- package-item-number-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [elements/text-field ::package-item-number-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :item-number
                             :placeholder "xxx-xxx"
                             :value-path  [:packages :editor/edited-item :item-number]}]))

(defn- package-automatic-price
  []
  (let [editor-disabled?        @(r/subscribe [:item-editor/editor-disabled? :packages.editor])
        package-automatic-price @(r/subscribe [:x.db/get-item [:packages :editor/edited-item :automatic-price]])]
       [common/data-element ::package-automatic-price
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :unit-price
                             :placeholder "-"
                             :value       {:content package-automatic-price :suffix " EUR"}}]))

(defn- package-price-field
  []
  (let [editor-disabled?        @(r/subscribe [:item-editor/editor-disabled? :packages.editor])
        package-automatic-price @(r/subscribe [:x.db/get-item [:packages :editor/edited-item :automatic-price]])]
       [elements/text-field ::package-price-field
                            {:disabled?      editor-disabled?
                             :end-adornments [{:label "EUR" :color :muted}]
                             :indent         {:top :m :vertical :s}
                             :label          :unit-price
                             :placeholder    package-automatic-price
                             :required?      true
                             :value-path     [:packages :editor/edited-item :unit-price]}]))

(defn- package-price
  []
  (if-let [automatic-pricing? @(r/subscribe [:x.db/get-item [:packages :editor/edited-item :automatic-pricing?]])]
          [package-automatic-price]
          [package-price-field]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [elements/combo-box ::package-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:packages :editor/suggestions :name]
                            :placeholder  :package-name-placeholder
                            :required?    true
                            :value-path   [:packages :editor/edited-item :name]}]))

(defn- package-description-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [elements/multiline-field ::package-description-field
                                 {:disabled?   editor-disabled?
                                  :indent      {:top :m :vertical :s}
                                  :label       :description
                                  :placeholder :package-description-placeholder
                                  :value-path  [:packages :editor/edited-item :description]}]))

(defn- package-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [common/surface-box ::package-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 66})
                                                      [package-name-field]]
                                                [:div (forms/form-block-attributes {:ratio 34})]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [package-quantity-unit-select]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [package-price]]
                                                [:div (forms/form-block-attributes {:ratio 34})
                                                      [package-item-number-field]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [package-description-field]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-data
  []
  [:<> [package-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :packages.editor])]
       (case current-view-id :data      [package-data]
                             :thumbnail [package-thumbnail]
                             :settings  [package-settings])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [common/item-editor-menu-bar :packages.editor
                                    {:disabled?  editor-disabled?
                                     :menu-items [{:label :data      :change-keys [:name :unit-price :description :quantity-unit :item-number]}
                                                  {:label :thumbnail :change-keys [:thumbnail]}
                                                  {:label :settings  :change-keys [:automatic-pricing?]}]}]))

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [common/item-editor-controls :packages.editor
                                       {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled?   @(r/subscribe [:item-editor/editor-disabled? :packages.editor])
        package-name @(r/subscribe [:x.db/get-item [:packages :editor/edited-item :name]])
        package-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        package-uri   (str "/@app-home/packages/" package-id)]
       [common/surface-breadcrumbs :packages.editor/view
                                   {:crumbs (if package-id [{:label :app-home    :route "/@app-home"}
                                                            {:label :packages    :route "/@app-home/packages"}
                                                            {:label package-name :route package-uri :placeholder :unnamed-package}
                                                            {:label :edit!}]
                                                           [{:label :app-home    :route "/@app-home"}
                                                            {:label :packages    :route "/@app-home/packages"}
                                                            {:label :add!}])
                                    :disabled? editor-disabled?}]))

(defn- label
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])
        package-name     @(r/subscribe [:x.db/get-item [:packages :editor/edited-item :name]])]
       [common/surface-label :packages.editor/view
                             {:disabled?   editor-disabled?
                              :label       package-name
                              :placeholder :unnamed-package}]))

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

(defn- package-editor
  []
  [item-editor/body :packages.editor
                    {:auto-title?      true
                     :form-element     #'view-structure
                     :error-element    [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-editor-ghost-element
                     :initial-item     {:automatic-pricing? true :quantity-unit {:label :unit :value :n-units}}
                     :item-path        [:packages :editor/edited-item]
                     :label-key        :name
                     :suggestion-keys  [:name]
                     :suggestions-path [:packages :editor/suggestions]}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'package-editor}])
