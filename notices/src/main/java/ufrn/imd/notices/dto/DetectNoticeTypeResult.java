package ufrn.imd.notices.dto;

import ufrn.imd.notices.models.enums.NoticeType;

public record DetectNoticeTypeResult(
  NoticeType type
) {};
