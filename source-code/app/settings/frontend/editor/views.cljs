
(ns app.settings.frontend.editor.views
    (:require [app.common.frontend.api            :as common]
              [app.components.frontend.api        :as components]
              [app.settings.frontend.editor.boxes :as editor.boxes]
              [elements.api                       :as elements]
              [layouts.surface-a.api              :as surface-a]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- settings-sales
  []
  [:<> [editor.boxes/settings-taxes-and-current-price-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :settings.editor])]
       (case current-view-id :sales [settings-sales])))

(defn- body
  []
  [common/item-editor-body :settings.editor
                           {:form-element [view-selector]
                            :item-path    [:settings :editor/edited-item]
                            :on-saved     [:settings.editor/user-settings-saved]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [common/item-editor-header :settings.editor
                             {:label       :settings
                              :crumbs      [{:label :app-home :route "/@app-home"}
                                            {:label :settings}]
                              :menu-items  [{:label :appearance    :disabled? true}
                                            {:label :notifications :disabled? true}
                                            {:label :privacy       :disabled? true}
                                            {:label :sales         :change-keys []}]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]]}])
