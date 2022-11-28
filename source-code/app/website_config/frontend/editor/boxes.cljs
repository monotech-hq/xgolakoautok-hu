
(ns app.website-config.frontend.editor.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.storage.frontend.api    :as storage]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- meta-name-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/text-field ::meta-name-field
                            {:disabled?   editor-disabled?
                             :label       :meta-name
                             :indent      {:top :m :vertical :s}
                             :info-text   :describe-the-page-with-a-name
                             :placeholder :meta-name-placeholder
                             :value-path  [:website-config :editor/edited-item :meta-name]}]))

(defn- meta-title-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/text-field ::meta-title-field
                            {:disabled?   editor-disabled?
                             :label       :meta-title
                             :indent      {:top :m :vertical :s}
                             :info-text   :describe-the-page-with-a-short-title
                             :placeholder :meta-title-placeholder
                             :value-path  [:website-config :editor/edited-item :meta-title]}]))

(defn- meta-description-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/multiline-field ::meta-description-field
                                 {:disabled?   editor-disabled?
                                  :label       :meta-description
                                  :indent      {:top :m :vertical :s}
                                  :info-text   :describe-the-page-with-a-short-description
                                  :placeholder :meta-description-placeholder
                                  :value-path  [:website-config :editor/edited-item :meta-description]}]))

(defn- meta-keywords-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/multi-combo-box ::meta-keywords-field
                                 {:deletable?  true
                                  :disabled?   editor-disabled?
                                  :label       :meta-keywords
                                  :indent      {:top :m :vertical :s}
                                  :info-text   :describe-the-page-in-a-few-keywords
                                  :placeholder :meta-keywords-placeholder
                                  :value-path  [:website-config :editor/edited-item :meta-keywords]}]))

(defn- seo-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [components/surface-box ::seo-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [meta-name-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [meta-title-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [meta-keywords-field]]
                                               [:div (forms/form-row-attributes)
                                                     [:div (forms/form-block-attributes {:ratio 100})
                                                           [meta-description-field]]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :seo}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- share-preview-picker
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [storage/media-picker ::share-preview-picker
                             {:autosave?     true
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? false
                              :placeholder   "n/a"
                              :toggle-label  :select-image!
                              :value-path    [:website-config :editor/edited-item :share-preview]}]))

(defn- share-preview-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [components/surface-box ::share-preview-box
                               {:content [:<> [share-preview-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :info-text {:content :recommended-image-size-n :replacements ["1200" "630"]}
                                :label     :share-preview}]))
