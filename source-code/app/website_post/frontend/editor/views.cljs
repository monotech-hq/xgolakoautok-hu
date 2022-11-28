
(ns app.website-post.frontend.editor.views
    (:require [app.common.frontend.api                :as common]
              [app.components.frontend.api            :as components]
              [app.website-post.frontend.editor.boxes :as editor.boxes]
              [elements.api                           :as elements]
              [layouts.surface-a.api                  :as surface-a]
              [re-frame.api                           :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- server-data
  []
  [:<> [editor.boxes/server-data-box]])

(defn- basic-data
  []
  [:<> [editor.boxes/basic-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :website-post.editor])]
       (case current-view-id :basic-data  [basic-data]
                             :server-data [server-data])))

(defn- body
  []
  [common/file-editor-body :website-post.editor
                           {:content-path [:website-post :editor/edited-item]
                            :form-element [view-selector]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [common/file-editor-header :website-post.editor
                             {:label       :website-post
                              :crumbs      [{:label :app-home :route "/@app-home"}
                                            {:label :website-post}]
                              :menu-items  [{:label :basic-data  :change-keys [:receiver-email-address :email-subject :email-body]}
                                            {:label :server-data :change-keys [:email-server-address :email-server-port
                                                                               :account-password :account-username]}]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]]}])
