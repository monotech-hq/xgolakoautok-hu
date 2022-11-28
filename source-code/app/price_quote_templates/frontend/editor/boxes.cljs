
(ns app.price-quote-templates.frontend.editor.boxes
  (:require [app.common.frontend.api                      :as common]
            [app.components.frontend.api                  :as components]
            [app.contents.frontend.api                    :as contents]
            [app.storage.frontend.api                     :as storage]
            [app.price-quote-templates.mid.handler.config :as handler.config]
            [elements.api                                 :as elements]
            [forms.api                                    :as forms]
            [re-frame.api                                 :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-footer-content-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [elements/multiline-field ::template-footer-content-field
                                 {:disabled?   editor-disabled?
                                  :indent      {:top :m :vertical :s}
                                  :info-text   :footer-content-helper
                                  :label       :footer-content
                                  :placeholder :footer-content-placeholder
                                  :value-path  [:price-quote-templates :editor/edited-item :footer-content]}]))

(defn- template-footer-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [components/surface-box ::template-footer-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-footer-content-field]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :footer}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-informations-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [contents/content-picker ::template-informations-picker
                                {:autosave?     true
                                 :color         :muted
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? false
                                 :placeholder   "n/a"
                                 :value-path    [:price-quote-templates :editor/edited-item :informations]}]))

(defn- template-informational-content-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [components/surface-box ::template-informational-content-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-informations-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :informational-content}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-issuer-details-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [elements/multiline-field ::template-issuer-details-field
                                 {:disabled?   editor-disabled?
                                  :indent      {:top :m :vertical :s}
                                  :info-text   :issuer-details-helper
                                  :label       :issuer-description
                                  :placeholder :issuer-details-placeholder
                                  :value-path  [:price-quote-templates :editor/edited-item :issuer-details]}]))

(defn- template-issuer-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [elements/text-field ::template-issuer-name-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :issuer-name
                             :placeholder :issuer-name-placeholder
                             :required?   true
                             :value-path  [:price-quote-templates :editor/edited-item :issuer-name]}]))

(defn- template-issuer-details-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [components/surface-box ::template-issuer-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-issuer-name-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-issuer-details-field]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :indent    {:top :m}
                                :label     :issuer-details}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-issuer-logo-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [storage/media-picker ::template-issuer-logo-picker
                             {:autosave?     true
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? false
                              :placeholder   "n/a"
                              :toggle-label  :select-image!
                              :value-path    [:price-quote-templates :editor/edited-item :issuer-logo]}]))

(defn- template-issuer-logo-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [components/surface-box ::template-issuer-logo-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-issuer-logo-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :issuer-logo}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-body-description-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [elements/text-field ::template-body-description-field
                            {:disabled?    editor-disabled?
                             :indent       {:top :m :vertical :s}
                             :info-text    :body-description-helper
                             :label        :body-description
                             :placeholder  :body-description-placeholder
                             :value-path   [:price-quote-templates :editor/edited-item :body-description]}]))

(defn- template-body-subtitle-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [elements/text-field ::template-body-subtitle-field
                            {:disabled?    editor-disabled?
                             :indent       {:top :m :vertical :s}
                             :info-text    :body-subtitle-helper
                             :label        :body-subtitle
                             :placeholder  :body-subtitle-placeholder
                             :value-path   [:price-quote-templates :editor/edited-item :body-subtitle]}]))

(defn- template-body-title-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [elements/text-field ::template-body-title-field
                            {:disabled?    editor-disabled?
                             :indent       {:top :m :vertical :s}
                             :info-text    :body-title-helper
                             :label        :body-title
                             :placeholder  :body-title-placeholder
                             :value-path   [:price-quote-templates :editor/edited-item :body-title]}]))

(defn- template-body-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [components/surface-box ::template-body-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-body-title-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-body-subtitle-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-body-description-field]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :body}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-default-currency-select
  []
  ; Ha a primary-currency és secondary-currency értéke megegyezik, akkor csak egyet
  ; szükséges megjeleníteni a két megegyező értékből!
  (let [editor-disabled?   @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])
        primary-currency   @(r/subscribe [:x.user/get-user-settings-item :primary-currency])
        secondary-currency @(r/subscribe [:x.user/get-user-settings-item :secondary-currency])]
       [elements/select ::template-currency-select
                        {:disabled?    editor-disabled?
                         :indent       {:top :m :vertical :s}
                         :label        :default-currency
                         :options      (-> [primary-currency secondary-currency] set vec)
                         :required?    true
                         :value-path   [:price-quote-templates :editor/edited-item :default-currency]}]))

(defn- template-language-select
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [elements/select ::template-language-select
                        {:disabled?  editor-disabled?
                         :indent     {:top :m :vertical :s}
                         :label      :language
                         :options    [:hu :en]
                         :required?  true
                         :value-path [:price-quote-templates :editor/edited-item :language]}]))

(defn- template-settings-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [components/surface-box ::template-settings-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [template-language-select]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [template-default-currency-select]]
                                                    [:div (forms/form-block-attributes {:ratio 34})]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :settings}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [elements/combo-box ::template-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:price-quote-templates :editor/suggestions :name]
                            :placeholder  :price-quote-template-name-placeholder
                            :required?    true
                            :value-path   [:price-quote-templates :editor/edited-item :name]}]))

(defn- template-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quote-templates.editor])]
       [components/surface-box ::template-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-name-field]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :data}]))
