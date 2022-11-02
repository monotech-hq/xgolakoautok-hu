
(ns app.storage.frontend.media-browser.effects
    (:require [app.storage.frontend.media-browser.views :as media-browser.views]
              [engines.item-browser.api                 :as item-browser]
              [re-frame.api                             :as r :refer [r]]
              [window.api                               :as window]
              [x.app-media.api                          :as x.media]
              [x.app-router.api                         :as x.router]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.media-browser/load-browser!
  [:storage.media-browser/render-browser!])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.media-browser/create-directory!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage.media-browser)]
           [:storage.directory-creator/load-creator! {:browser-id :storage.media-browser :destination-id destination-id}])))

(r/reg-event-fx :storage.media-browser/upload-files!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage.media-browser)]
           [:storage.file-uploader/load-uploader! {:browser-id :storage.media-browser :destination-id destination-id}])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.media-browser/add-new-item!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage.media-browser)
            load-props     {:browser-id :storage.media-browser :destination-id destination-id}]
           (case selected-option :upload-files!     [:storage.file-uploader/load-uploader!    load-props]
                                 :create-directory! [:storage.directory-creator/load-creator! load-props]))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.media-browser/delete-item!
  (fn [_ [_ {:keys [id]}]]
      {:dispatch-n [[:ui/remove-popup! :storage.media-menu/view]
                    [:item-browser/delete-item! :storage.media-browser id]]}))

(r/reg-event-fx :storage.media-browser/duplicate-item!
  (fn [_ [_ {:keys [id]}]]
      {:dispatch-n [[:ui/remove-popup! :storage.media-menu/view]
                    [:item-browser/duplicate-item! :storage.media-browser id]]}))

(r/reg-event-fx :storage.media-browser/rename-item!
  (fn [_ [_ media-item]]
      {:dispatch-n [[:ui/remove-popup! :storage.media-menu/view]
                    [:storage.alias-editor/load-editor! media-item]]}))

(r/reg-event-fx :storage.media-browser/move-item!
  (fn [{:keys [db]} [_ media-item]]
      {:dispatch-n [[:ui/remove-popup! :storage.media-menu/view]]}))

;; -- Directory-item effect events --------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.media-browser/open-directory!
  (fn [_ [_ {:keys [id]}]]
      {:dispatch-n [[:ui/remove-popup! :storage.media-menu/view]
                    [:item-browser/browse-item! :storage.media-browser id]]}))

(r/reg-event-fx :storage.media-browser/copy-directory-link!
  (fn [{:keys [db]} [_ {:keys [id]}]]
      (let [directory-uri  (r item-browser/get-item-route db :storage.media-browser id)
            directory-uri  (r x.router/use-app-home       db directory-uri)
            directory-link (r x.router/use-app-domain     db directory-uri)]
           {:dispatch-n [[:ui/remove-popup! :storage.media-menu/view]
                         [:clipboard/copy-text! directory-link]]})))

;; -- File-item effect events -------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.media-browser/preview-file!
  (fn [{:keys [db]} [_ {:keys [filename]}]]
      (let [directory-id (r item-browser/get-current-item-id db :storage.media-browser)]
           {:dispatch-n [[:ui/remove-popup! :storage.media-menu/view]
                         [:storage.media-viewer/load-viewer! {:directory-id directory-id :current-item filename}]]})))

(r/reg-event-fx :storage.media-browser/download-file!
  (fn [{:keys [db]} [_ {:keys [alias filename]}]]
      {:dispatch-n [[:ui/remove-popup! :storage.media-menu/view]
                    [:tools/save-file! {:filename alias :uri (x.media/filename->media-storage-uri filename)}]]}))

(r/reg-event-fx :storage.media-browser/copy-file-link!
  (fn [{:keys [db]} [_ {:keys [filename]}]]
      (let [file-uri  (x.media/filename->media-storage-uri filename)
            file-link (r x.router/use-app-domain db file-uri)]
           {:dispatch-n [[:ui/remove-popup! :storage.media-menu/view]
                         [:clipboard/copy-text! file-link]]})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.media-browser/render-browser!
  [:ui/render-surface! :storage.media-browser/view
                       {:content #'media-browser.views/view}])
