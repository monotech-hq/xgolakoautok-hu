
(ns app.vehicle-categories.frontend.editor.views
    (:require [app.common.frontend.api  :as common]
              [app.storage.frontend.api :as storage]
              [elements.api             :as elements]
              [engines.item-editor.api  :as item-editor]
              [forms.api                :as forms]
              [layouts.surface-a.api    :as surface-a]
              [re-frame.api             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-public-link
  []
  (let [editor-disabled?     @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])
        category-public-link @(r/subscribe [:vehicle-categories.editor/get-category-public-link])]
       [common/data-element ::category-public-link
                            {:disabled? editor-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :public-link
                             :value     category-public-link}]))

(defn- category-visibility-radio-button
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [elements/radio-button ::category-visibility-radio-button
                              {:disabled?       editor-disabled?
                               :indent          {:top :m :vertical :s}
                               :label           :visibility-on-the-website
                               :options         [{:label :public-content  :helper :visible-to-everyone     :value :public}
                                                 {:label :private-content :helper :only-visible-to-editors :value :private}]
                               :option-helper-f :helper
                               :option-label-f  :label
                               :option-value-f  :value
                               :value-path      [:vehicle-categories :editor/edited-item :visibility]}]))

(defn- category-settings-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [common/surface-box ::category-settings-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [category-visibility-radio-button]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [category-public-link]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :settings}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-settings
  []
  [:<> [category-settings-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-thumbnail-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [storage/media-picker ::category-thumbnail-picker
                             {:autosave?     true
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? false
                              :placeholder   "-"
                              :toggle-label  :select-image!
                              :thumbnail     {:height :3xl :width :5xl}
                              :value-path    [:vehicle-categories :editor/edited-item :thumbnail]}]))

(defn- category-thumbnail-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [common/surface-box ::category-thumbnail-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [category-thumbnail-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-thumbnail
  []
  [:<> [category-thumbnail-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-description-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [elements/multiline-field ::category-description-field
                                 {:disabled?   editor-disabled?
                                  :emptiable?  false
                                  :indent      {:top :m :vertical :s}
                                  :label       :description
                                  :placeholder :vehicle-category-description-placeholder
                                  :value-path  [:vehicle-categories :editor/edited-item :description]}]))

(defn- category-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [elements/combo-box ::category-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:vehicle-categories :editor/suggestions :name]
                            :placeholder  :vehicle-category-name-placeholder
                            :required?    true
                            :value-path   [:vehicle-categories :editor/edited-item :name]}]))

(defn- category-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [common/surface-box ::category-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [category-name-field]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [category-description-field]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-data
  []
  [:<> [category-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :vehicle-categories.editor])]
       (case current-view-id :data      [category-data]
                             :thumbnail [category-thumbnail]
                             :settings  [category-settings])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [common/item-editor-menu-bar :vehicle-categories.editor
                                    {:disabled?  editor-disabled?
                                     :menu-items [{:label :data      :change-keys [:name :description]}
                                                  {:label :thumbnail :change-keys [:thumbnail]}
                                                  {:label :settings  :change-keys [:visibility]}]}]))

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [common/item-editor-controls :vehicle-categories.editor
                                    {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])
        category-name    @(r/subscribe [:x.db/get-item [:vehicle-categories :editor/edited-item :name]])
        category-id      @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        category-uri      (str "/@app-home/vehicle-categories/" category-id)]
       [common/surface-breadcrumbs :vehicle-categories.editor/view
                                   {:crumbs (if category-id [{:label :app-home           :route "/@app-home"}
                                                             {:label :vehicle-categories :route "/@app-home/vehicle-categories"}
                                                             {:label category-name       :route category-uri :placeholder :unnamed-vehicle-category}
                                                             {:label :edit!}]
                                                            [{:label :app-home           :route "/@app-home"}
                                                             {:label :vehicle-categories :route "/@app-home/vehicle-categories"}
                                                             {:label :add!}])
                                    :disabled? editor-disabled?}]))

(defn- label
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])
        category-name    @(r/subscribe [:x.db/get-item [:vehicle-categories :editor/edited-item :name]])]
       [common/surface-label :vehicle-categories.editor/view
                             {:disabled?   editor-disabled?
                              :label       category-name
                              :placeholder :unnamed-vehicle-category}]))

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

(defn- category-editor
  []
  [item-editor/body :vehicle-categories.editor
                    {:auto-title?      true
                     :form-element     #'view-structure
                     :error-element    [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-editor-ghost-element
                     :initial-item     {:visibility :public}
                     :item-path        [:vehicle-categories :editor/edited-item]
                     :label-key        :name
                     :suggestion-keys  [:name]
                     :suggestions-path [:vehicle-categories :editor/suggestions]}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'category-editor}])
