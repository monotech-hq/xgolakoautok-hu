
(ns app.contents.frontend.editor.views
    (:require [app.common.frontend.api            :as common]
              [app.components.frontend.api        :as components]
              [app.contents.frontend.editor.boxes :as editor.boxes]
              [elements.api                       :as elements]
              [layouts.surface-a.api              :as surface-a]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-editor-footer :contents.editor {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-content
  []
  [:<> [editor.boxes/content-content-box]])

(defn- content-settings
  []
  [:<> [editor.boxes/content-settings-box]])

(defn- content-data
  []
  [:<> [editor.boxes/content-basic-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :contents.editor])]
       (case current-view-id :data     [content-data]
                             :content  [content-content]
                             :settings [content-settings])))

(defn- body
  []
  [common/item-editor-body :contents.editor
                           {:form-element     [view-selector]
                            :initial-item     {:visibility :public}
                            :item-path        [:contents :editor/edited-item]
                            :suggestions-path [:contents :editor/suggestions]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [content-name  @(r/subscribe [:x.db/get-item [:contents :editor/edited-item :name]])
        content-id    @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        content-route @(r/subscribe [:item-editor/get-item-route :contents.editor content-id])]
       [common/item-editor-header :contents.editor
                                  {:label       content-name
                                   :placeholder :unnamed-content
                                   :crumbs      [{:label :app-home    :route "/@app-home"}
                                                 {:label :contents    :route "/@app-home/contents"}
                                                 {:label content-name :route content-route :placeholder :unnamed-content}]
                                   :menu-items  [{:label :data     :change-keys [:name]}
                                                 {:label :content  :change-keys [:body]}
                                                 {:label :settings :change-keys [:visibility]}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
