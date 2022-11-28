
(ns app.website-dictionary.frontend.editor.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [engines.file-editor.api     :as file-editor]
              [forms.api                   :as forms]
              [layouts.surface-a.api       :as surface-a]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- dictionary-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-dictionary.editor])]
       [components/surface-box ::dictionary-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})]
                                                    [:div (forms/form-block-attributes {:ratio 67})]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :dictionary}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- dictionary
  []
  [:<> [dictionary-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :website-dictionary.editor])]
       (case current-view-id :dictionary [dictionary])))

(defn- body
  []
  [common/file-editor-body :website-dictionary.editor
                           {:content-path [:website-dictionary :editor/edited-item]
                            :form-element [view-selector]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [common/file-editor-header :website-dictionary.editor
                             {:label       :website-dictionary
                              :crumbs      [{:label :app-home :route "/@app-home"}
                                            {:label :website-dictionary}]
                              :menu-items  [{:label :dictionary :change-keys []}]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]]}])
