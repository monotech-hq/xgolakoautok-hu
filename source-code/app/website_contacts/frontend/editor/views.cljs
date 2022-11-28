
(ns app.website-contacts.frontend.editor.views
    (:require [app.common.frontend.api                    :as common]
              [app.components.frontend.api                :as components]
              [app.website-contacts.frontend.editor.boxes :as editor.boxes]
              [elements.api                               :as elements]
              [engines.file-editor.api                    :as file-editor]
              [layouts.surface-a.api                      :as surface-a]
              [re-frame.api                               :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- contacts-data
  []
  [:<> [editor.boxes/contacts-data-box]
       [editor.boxes/contacts-data-information-box]
       [editor.boxes/contact-group-list]])

(defn- address-data
  []
  [:<> [editor.boxes/address-data-box]
       [editor.boxes/address-data-information-box]
       [editor.boxes/address-group-list]])

(defn- social-media
  []
  [:<> [editor.boxes/social-media-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :website-contacts.editor])]
       (case current-view-id :contacts-data [contacts-data]
                             :address-data  [address-data]
                             :social-media  [social-media])))

(defn- body
  []
  [common/file-editor-body :website-contacts.editor
                           {:content-path [:website-contacts :editor/edited-item]
                            :form-element [view-selector]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [common/file-editor-header :website-contacts.editor
                             {:label       :website-contacts
                              :crumbs      [{:label :app-home :route "/@app-home"}
                                            {:label :website-contacts}]
                              :menu-items  [{:label :contacts-data :change-keys [:contact-groups :contacts-data-information]}
                                            {:label :address-data  :change-keys [:address-groups :address-data-information]}
                                            {:label :social-media  :change-keys [:facebook-links :instagram-links :youtube-links]}]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]]}])
