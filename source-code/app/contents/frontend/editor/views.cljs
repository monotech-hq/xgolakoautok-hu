
(ns app.contents.frontend.editor.views
    (:require [app.common.frontend.api  :as common]
              [app.storage.frontend.api :as storage]
              [elements.api             :as elements]
              [engines.item-editor.api  :as item-editor]
              [engines.text-editor.api  :as text-editor]
              [forms.api                :as forms]
              [layouts.surface-a.api    :as surface-a]
              [re-frame.api             :as r]))

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
       [common/surface-box ::content-content-box
                           {:content [:<> [content-body-editor]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :content}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-content
  []
  [:<> [content-content-box]])

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
       [common/surface-box ::content-settings-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [content-visibility-radio-button]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :label     :settings
                            :disabled? editor-disabled?}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-settings
  []
  [:<> [content-settings-box]])

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
       [common/surface-box ::content-basic-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [content-name-field]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :basic-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-data
  []
  [:<> [content-basic-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :contents.editor])]
       [common/item-editor-menu-bar :contents.editor
                                    {:disabled?  editor-disabled?
                                     :menu-items [{:label :data     :change-keys [:name]}
                                                  {:label :content  :change-keys [:body]}
                                                  {:label :settings :change-keys [:visibility]}]}]))

(defn- body
  []
  (let [current-view-id @(r/subscribe [:gestures/get-current-view-id :contents.editor])]
       (case current-view-id :data     [content-data]
                             :content  [content-content]
                             :settings [content-settings])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :contents.editor])]
       [common/item-editor-controls :contents.editor
                                    {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :contents.editor])
        content-name     @(r/subscribe [:db/get-item [:contents :editor/edited-item :name]])
        content-id       @(r/subscribe [:router/get-current-route-path-param :item-id])
        content-uri       (str "/@app-home/contents/" content-id)]
       [common/surface-breadcrumbs :contents.editor/view
                                   {:crumbs (if content-id [{:label :app-home    :route "/@app-home"}
                                                            {:label :contents    :route "/@app-home/contents"}
                                                            {:label content-name :route content-uri :placeholder :unnamed-content}
                                                            {:label :edit!}]
                                                           [{:label :app-home    :route "/@app-home"}
                                                            {:label :contents    :route "/@app-home/contents"}
                                                            {:label :add!}])
                                    :disabled? editor-disabled?}]))

(defn- label
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :contents.editor])
        content-name     @(r/subscribe [:db/get-item [:contents :editor/edited-item :name]])]
       [common/surface-label :contents.editor/view
                             {:disabled?   editor-disabled?
                              :label       content-name
                              :placeholder :unnamed-content}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "48px"}}
             [:div [label]
                   [breadcrumbs]]
             [:div [controls]]]
       [elements/horizontal-separator {:size :xxl}]
       [menu-bar]])

(defn- view-structure
  []
  [:<> [header]
       [body]])

(defn- content-editor
  []
  [item-editor/body :contents.editor
                    {:auto-title?      true
                     :form-element     #'view-structure
                     :error-element    [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-editor-ghost-element
                     :initial-item     {:visibility :public}
                     :item-path        [:contents :editor/edited-item]
                     :label-key        :name
                     :suggestion-keys  [:name]
                     :suggestions-path [:contents :editor/suggestions]}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'content-editor}])
