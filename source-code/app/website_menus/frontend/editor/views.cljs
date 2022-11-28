
(ns app.website-menus.frontend.editor.views
    (:require [app.common.frontend.api                 :as common]
              [app.components.frontend.api             :as components]
              [app.website-menus.frontend.editor.boxes :as editor.boxes]
              [elements.api                            :as elements]
              [layouts.surface-a.api                   :as surface-a]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-editor-footer :website-menus.editor {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-items
  []
  [:<> [editor.boxes/menu-menu-items-box]])

(defn- menu-data
  []
  [:<> [editor.boxes/menu-basic-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :website-menus.editor])]
       (case current-view-id :data       [menu-data]
                             :menu-items [menu-items])))

(defn- body
  []
  [common/item-editor-body :website-menus.editor
                           {:form-element     [view-selector]
                            :item-path        [:website-menus :editor/edited-item]
                            :suggestions-path [:website-menus :editor/suggestions]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [menu-name  @(r/subscribe [:x.db/get-item [:website-menus :editor/edited-item :name]])
        menu-id    @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        menu-route @(r/subscribe [:item-editor/get-item-route :website-menus.editor menu-id])]
       [common/item-editor-header :website-menus.editor
                                  {:label       menu-name
                                   :placeholder :unnamed-website-menu
                                   :crumbs      [{:label :app-home      :route "/@app-home"}
                                                 {:label :website-menus :route "/@app-home/website-menus"}
                                                 {:label menu-name      :route menu-route :placeholder :unnamed-website-menu}]
                                   :menu-items  [{:label :data       :change-keys [:name]}
                                                 {:label :menu-items :change-keys [:menu-items]}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
