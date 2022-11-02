
(ns app.storage.frontend.media-browser.lifecycles
    (:require [app.home.frontend.api]
              [x.app-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group       :website
                                                            :icon        :folder
                                                            :icon-color  "#8655b1"
                                                            :icon-family :material-icons-outlined
                                                            :label       :file-storage
                                                            :on-click    [:router/go-to! "/@app-home/storage"]
                                                            :horizontal-weight 0}]
                              [:home.sidebar/add-menu-item! {:group       :website
                                                             :icon        :folder
                                                             :icon-color  "#8655b1"
                                                             :icon-family :material-icons-outlined
                                                             :label       :file-storage
                                                             :on-click    [:router/go-to! "/@app-home/storage"]
                                                             :vertical-weight 0}]]}})
