
(ns app.website-pages.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group-name  :content
                                                            :icon        :description
                                                            :icon-color  "#5564b1"
                                                            :icon-family :material-icons-outlined
                                                            :label       :website-pages
                                                            :on-click    [:x.router/go-to! "/@app-home/website-pages"]
                                                            :horizontal-weight 0}]
                              [:home.sidebar/add-menu-item! {:group-name  :content
                                                             :icon        :description
                                                             :icon-color  "#5c72e5"
                                                             :icon-family :material-icons-outlined
                                                             :label       :website-pages
                                                             :on-click    [:x.router/go-to! "/@app-home/website-pages"]
                                                             :vertical-weight 0}]]}})
