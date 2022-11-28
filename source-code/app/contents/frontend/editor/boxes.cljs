
(ns app.contents.frontend.editor.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [engines.text-editor.api     :as text-editor]
              [forms.api                   :as forms]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-body-editor
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :contents.editor])]
       [text-editor/body ::content-body-editor
                         {:disabled?  editor-disabled?
                          :indent     {:top :m :vertical :s}
                          :value-path [:contents :editor/edited-item :body]}]))

(defn- content-content-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :contents.editor])]
       [components/surface-box ::content-content-box
                               {:content [:<> [content-body-editor]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :content}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-visibility-radio-button
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :contents.editor])]
       [elements/radio-button ::content-visibility-radio-button
                              {:disabled?       editor-disabled?
                               :indent          {:top :m :vertical :s}
                               :label           :content-visibility
                               :options         [{:label :public-content  :helper :visible-to-everyone     :value :public}
                                                 {:label :private-content :helper :only-visible-to-editors :value :private}]
                               :option-helper-f :helper
                               :option-label-f  :label
                               :option-value-f  :value
                               :value-path      [:contents :editor/edited-item :visibility]}]))

(defn- content-settings-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :contents.editor])]
       [components/surface-box ::content-settings-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [content-visibility-radio-button]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :label     :settings
                                :disabled? editor-disabled?}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :contents.editor])]
       [elements/combo-box ::content-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:contents :editor/suggestions :name]
                            :placeholder  :content-name-placeholder
                            :required?    true
                            :value-path   [:contents :editor/edited-item :name]}]))

(defn- content-basic-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :contents.editor])]
       [components/surface-box ::content-basic-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [content-name-field]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :basic-data}]))
