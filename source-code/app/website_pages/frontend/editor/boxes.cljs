
(ns app.website-pages.frontend.editor.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.contents.frontend.api   :as contents]
              [elements.api                :as elements]
              [engines.text-editor.api     :as text-editor]
              [forms.api                   :as forms]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- page-content-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-pages.editor])]
       [contents/content-picker ::page-content-picker
                                {:autosave?     true
                                 :color         :muted
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? false
                                 :placeholder   "n/a"
                                 :value-path    [:website-pages :editor/edited-item :content]}]))

(defn- page-content-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-pages.editor])]
       [components/surface-box ::page-content-box
                               {:content [:<> [page-content-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :content}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- page-automatic-link-checkbox
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-pages.editor])]
       [elements/checkbox ::page-automatic-link-checkbox
                          {:disabled?       editor-disabled?
                           :indent          {:top :m :vertical :s}
                           :options         [{:label :automatic-link :helper :website-page-automatic-link-helper :value true}]
                           :option-helper-f :helper
                           :option-label-f  :label
                           :option-value-f  :value
                           :value-path      [:website-pages :editor/edited-item :automatic-link?]}]))

(defn- page-visibility-radio-button
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-pages.editor])]
       [elements/radio-button ::page-visibility-radio-button
                              {:disabled?       editor-disabled?
                               :indent          {:top :m :vertical :s}
                               :label           :page-visibility
                               :options         [{:label :public-page  :helper :visible-to-everyone     :value :public}
                                                 {:label :private-page :helper :only-visible-to-editors :value :private}]
                               :option-helper-f :helper
                               :option-label-f  :label
                               :option-value-f  :value
                               :value-path      [:website-pages :editor/edited-item :visibility]}]))

(defn- page-settings-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-pages.editor])]
       [components/surface-box ::page-settings-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 50})
                                                          [page-visibility-radio-button]]
                                                    [:div (forms/form-block-attributes {:ratio 50})
                                                          [page-automatic-link-checkbox]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :label     :settings
                                :disabled? editor-disabled?}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- page-automatic-public-link
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-pages.editor])
        page-name        @(r/subscribe [:x.db/get-item [:website-pages :editor/edited-item :name]])
        page-public-link @(r/subscribe [:x.router/get-page-public-link page-name])]
       [components/data-element ::page-automatic-public-link
                                {:disabled? editor-disabled?
                                 :indent    {:top :m :vertical :s}
                                 :label     :public-link
                                 :value     page-public-link}]))

(defn- page-public-link-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-pages.editor])
        page-name        @(r/subscribe [:x.db/get-item [:website-pages :editor/edited-item :name]])
        page-public-link @(r/subscribe [:x.router/get-page-public-link page-name])]
       [elements/text-field ::page-public-link-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :public-link
                             :placeholder page-public-link
                             :required?   true
                             :value-path  [:website-pages :editor/edited-item :public-link]}]))

(defn- page-public-link
  []
  (if-let [automatic-link? @(r/subscribe [:x.db/get-item [:website-pages :editor/edited-item :automatic-link?]])]
          [page-automatic-public-link]
          [page-public-link-field]))

(defn- page-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-pages.editor])]
       [elements/combo-box ::page-name-field
                           {:disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:website-pages :editor/suggestions :name]
                            :placeholder  :website-page-name-placeholder
                            :required?    true
                            :value-path   [:website-pages :editor/edited-item :name]}]))

(defn- page-basic-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-pages.editor])]
       [components/surface-box ::page-basic-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [page-name-field]]
                                               [:div (forms/form-row-attributes)
                                                     [:div (forms/form-block-attributes {:ratio 100})
                                                           [page-public-link]]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :basic-data}]))
