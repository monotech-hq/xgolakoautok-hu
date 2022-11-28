
(ns app.website-pages.frontend.editor.views
    (:require [app.common.frontend.api                 :as common]
              [app.components.frontend.api             :as components]
              [app.website-pages.frontend.editor.boxes :as editor.boxes]
              [elements.api                            :as elements]
              [layouts.surface-a.api                   :as surface-a]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-editor-footer :website-pages.editor {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- page-settings
  []
  [:<> [editor.boxes/page-settings-box]])

(defn- page-content
  []
  [:<> [editor.boxes/page-content-box]])

(defn- page-data
  []
  [:<> [editor.boxes/page-basic-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :website-pages.editor])]
       (case current-view-id :data     [page-data]
                             :content  [page-content]
                             :settings [page-settings])))

(defn- body
  []
  [common/item-editor-body :website-pages.editor
                           {:form-element     [view-selector]
                            :initial-item     {:automatic-link? true :visibility :public}
                            :item-path        [:website-pages :editor/edited-item]
                            :suggestions-path [:website-pages :editor/suggestions]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [page-name  @(r/subscribe [:x.db/get-item [:website-pages :editor/edited-item :name]])
        page-id    @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        page-route @(r/subscribe [:item-editor/get-item-route :website-pages.editor page-id])]
       [common/item-editor-header :website-pages.editor
                                  {:label       page-name
                                   :placeholder :unnamed-website-page
                                   :crumbs      [{:label :app-home      :route "/@app-home"}
                                                 {:label :website-pages :route "/@app-home/website-pages"}
                                                 {:label page-name      :route page-route :placeholder :unnamed-website-page}]
                                   :menu-items  [{:label :data     :change-keys [:name :public-link]}
                                                 {:label :content  :change-keys [:content]}
                                                 {:label :settings :change-keys [:automatic-link? :visibility]}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
