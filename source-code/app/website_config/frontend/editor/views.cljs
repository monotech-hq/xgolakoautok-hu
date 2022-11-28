
(ns app.website-config.frontend.editor.views
    (:require [app.common.frontend.api                  :as common]
              [app.components.frontend.api              :as components]
              [app.website-config.frontend.editor.boxes :as editor.boxes]
              [elements.api                             :as elements]
              [engines.file-editor.api                  :as file-editor]
              [layouts.surface-a.api                    :as surface-a]
              [re-frame.api                             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- seo
  []
  [:<> [editor.boxes/seo-box]])

(defn- share
  []
  [:<> [editor.boxes/share-preview-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :website-config.editor])]
       (case current-view-id :seo   [seo]
                             :share [share])))

(defn- body
  []
  [common/file-editor-body :website-config.editor
                           {:content-path [:website-config :editor/edited-item]
                            :form-element [view-selector]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [common/file-editor-header :website-config.editor
                             {:label       :website-config
                              :crumbs      [{:label :app-home :route "/@app-home"}
                                            {:label :website-config}]
                              :menu-items  [{:label :seo   :change-keys [:meta-name :meta-title :meta-keywords :meta-description]}
                                            {:label :share :change-keys [:share-preview]}]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]]}])
