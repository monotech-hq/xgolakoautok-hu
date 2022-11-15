
(ns app.website-dictionary.frontend.editor.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.file-editor.api :as file-editor]
              [forms.api               :as forms]
              [layouts.surface-a.api   :as surface-a]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- dictionary-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-dictionary.editor])]
       [common/surface-box ::dictionary-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})]
                                                [:div (forms/form-block-attributes {:ratio 67})]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :dictionary}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- dictionary
  []
  [:<> [dictionary-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-dictionary.editor])]
       [common/file-editor-menu-bar :website-dictionary.editor
                                    {:menu-items [{:label :dictionary :change-keys []}]
                                     :disabled? editor-disabled?}]))

(defn- body
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :website-dictionary.editor])]
       (case current-view-id :dictionary [dictionary])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-dictionary.editor])]
       [common/file-editor-controls :website-dictionary.editor
                                    {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-dictionary.editor])]
       [common/surface-breadcrumbs :website-dictionary.editor/view
                                   {:crumbs [{:label :app-home :route "/@app-home"}
                                             {:label :website-dictionary}]
                                    :disabled? editor-disabled?}]))

(defn- label
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-dictionary.editor])]
       [common/surface-label :website-dictionary.editor/view
                             {:disabled? editor-disabled?
                              :label     :website-dictionary}]))

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

(defn- website-dictionary-editor
  ; @param (keyword) surface-id
  [_]
  [file-editor/body :website-dictionary.editor
                    {:content-path  [:website-dictionary :editor/edited-item]
                     :form-element  #'view-structure
                     :error-element [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element #'common/file-editor-ghost-element}])

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'website-dictionary-editor}])
