
(ns app.storage.frontend.media-menu.effects
    (:require [app.storage.frontend.media-menu.views :as media-menu.views]
              [re-frame.api                          :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.media-menu/render-directory-menu!
  (fn [{:keys [db]} [_ directory-item]]
      [:ui/render-popup! :storage.media-menu/view
                         {:content [media-menu.views/directory-menu directory-item]}]))

(r/reg-event-fx :storage.media-menu/render-file-menu!
  (fn [{:keys [db]} [_ file-item]]
      [:ui/render-popup! :storage.media-menu/view
                         {:content [media-menu.views/file-menu file-item]}]))
